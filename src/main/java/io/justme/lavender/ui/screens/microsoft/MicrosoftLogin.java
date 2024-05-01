package io.justme.lavender.ui.screens.microsoft;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

/**
 * 微软登录
 * 作者: yalan
 * 参考的文章:
 *  https://zhuanlan.zhihu.com/p/344830045 << Minecraft新版验证方法的解决方案 >>
 *  https://wiki.vg/Zh:Microsoft_Authentication_Scheme << Microsoft Authentication Scheme >>
 *  HMCL源码
 *  FDP源码
 */

@SuppressWarnings("DuplicatedCode")
public final class MicrosoftLogin implements Closeable {
    private final String CLIENT_ID = "67b74668-ef33-49c3-a75c-18cbb2481e0c";
    private final String REDIRECT_URI = "http://localhost:3434/sad";
    private final String SCOPE = "XboxLive.signin%20offline_access";

    private final String URL = "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>"
            .replace("<client_id>",CLIENT_ID)
            .replace("<redirect_uri>",REDIRECT_URI)
            .replace("<scope>",SCOPE);

    public volatile String uuid = null;
    public volatile String userName = null;
    public volatile String accessToken = null;
    public volatile String refreshToken = null;

    public volatile boolean logged = false;
    public volatile String status = EnumChatFormatting.YELLOW + "Logging...";

    private final HttpServer httpServer;

    @SuppressWarnings("FieldCanBeLocal")
    private final MicrosoftHttpHandler handler;

    public MicrosoftLogin() throws IOException {
        handler = new MicrosoftHttpHandler();
        httpServer = HttpServer.create(new InetSocketAddress("localhost",3434),0);
        httpServer.createContext("/sad",handler);
        httpServer.start();
    }

    public MicrosoftLogin(String refreshToken) throws IOException {
        this.refreshToken = refreshToken;
        this.httpServer = null;
        this.handler = null;

        final String microsoftTokenAndRefreshToken = getMicrosoftTokenFromRefreshToken(refreshToken);
        final String xBoxLiveToken = getXBoxLiveToken(microsoftTokenAndRefreshToken);
        final String[] xstsTokenAndHash = getXSTSTokenAndUserHash(xBoxLiveToken);
        final String accessToken = getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
        final URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        final String read = read(connection.getInputStream());
        final JSONObject jsonObject = JSON.parseObject(read);
        final String uuid = jsonObject.getString("id");
        final String userName = jsonObject.getString("name");

        MicrosoftLogin.this.uuid = uuid;
        MicrosoftLogin.this.userName = userName;
        MicrosoftLogin.this.accessToken = accessToken;
        MicrosoftLogin.this.logged = true;
    }

    @Override
    public void close() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    public void show() throws Exception {
        //Sys.openURL(URL);//Not Working @ Linux

        browse(URL);
    }

    private void browse(String url) throws Exception {
        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            // 苹果的打开方式
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",
                    String.class);
            openURL.invoke(null, url);
        } else if (osName.startsWith("Windows")) {
            // windows的打开方式。
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
                    "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                // 执行代码，在brower有值后跳出，
                // 这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime()
                        .exec(new String[] { "which", browsers[count] })
                        .waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                // 这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }

    private String getAccessToken(String xstsToken,String uhs) throws IOException {
        GuiMicrosoftLogin.much = 85;
        status = EnumChatFormatting.YELLOW + "Getting access token";

        final URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        final JSONObject input = new JSONObject(true);
        input.put("identityToken","XBL3.0 x=" + uhs + ";" + xstsToken);

        write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),JSON.toJSONString(input));

        final JSONObject jsonObject = JSON.parseObject(read(connection.getInputStream()));

        return jsonObject.getString("access_token");
    }

    public String getMicrosoftTokenFromRefreshToken(String refreshToken) throws IOException {
        GuiMicrosoftLogin.much = 25;
        status = EnumChatFormatting.YELLOW + "Getting microsoft token from refresh token";

        final URL url = new URL("https://login.live.com/oauth20_token.srf");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        final String param = "client_id=" + CLIENT_ID +
                //"&client_secret=" + "" +
                "&refresh_token=" + refreshToken +
                "&grant_type=refresh_token" +
                "&redirect_uri=" + REDIRECT_URI;

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),param);

        final JSONObject response_obj = JSON.parseObject(read(connection.getInputStream()));
        return response_obj.getString("access_token");
    }

    public String[] getMicrosoftTokenAndRefreshToken(String code) throws IOException {
        GuiMicrosoftLogin.much = 15;
        status = EnumChatFormatting.YELLOW + "Getting microsoft token";

        final URL url = new URL("https://login.live.com/oauth20_token.srf");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        final String param = "client_id=" + CLIENT_ID +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=" + SCOPE;

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),param);

        final JSONObject response_obj = JSON.parseObject(read(connection.getInputStream()));
        return new String[]{response_obj.getString("access_token"),response_obj.getString("refresh_token")};
    }

    @SuppressWarnings("HttpUrlsUsage")
    public String getXBoxLiveToken(String microsoftToken) throws IOException {
        GuiMicrosoftLogin.much = 40;
        status = EnumChatFormatting.YELLOW + "Getting xbox live token";


        final URL connectUrl = new URL("https://user.auth.xboxlive.com/user/authenticate");
        final String param;
        final JSONObject xbl_param = new JSONObject(true);
        final JSONObject xbl_properties = new JSONObject(true);
        xbl_properties.put("AuthMethod","RPS");
        xbl_properties.put("SiteName","user.auth.xboxlive.com");
        xbl_properties.put("RpsTicket","d=" + microsoftToken);
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","http://auth.xboxlive.com");
        xbl_param.put("TokenType","JWT");
        param = JSON.toJSONString(xbl_param);
        final HttpURLConnection connection = (HttpURLConnection) connectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Accept","application/json");

        write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),param);

        final JSONObject response_obj = JSON.parseObject(read(connection.getInputStream()));
        return response_obj.getString("Token");
    }

    public String[] getXSTSTokenAndUserHash(String xboxLiveToken) throws IOException {
        GuiMicrosoftLogin.much = 65;
        status = EnumChatFormatting.YELLOW + "Getting xsts token and user hash";


        final URL ConnectUrl = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
        final String param;
        final ArrayList<String> tokens = new ArrayList<>();
        tokens.add(xboxLiveToken);
        final JSONObject xbl_param = new JSONObject(true);
        final JSONObject xbl_properties = new JSONObject(true);
        xbl_properties.put("SandboxId","RETAIL");
        xbl_properties.put("UserTokens", JSONArray.parse(JSON.toJSONString(tokens)));
        xbl_param.put("Properties",xbl_properties);
        xbl_param.put("RelyingParty","rp://api.minecraftservices.com/");
        xbl_param.put("TokenType","JWT");
        param = JSON.toJSONString(xbl_param);
        final HttpURLConnection connection= (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");

        write(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),param);
        final JSONObject response_obj = JSON.parseObject(read(connection.getInputStream()));

        final String token = response_obj.getString("Token");
        final String uhs = response_obj.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs");
        return new String[]{token,uhs};
    }

    private void write(BufferedWriter writer,String s) throws IOException {
        writer.write(s);
        writer.close();
    }

    private String read(InputStream stream) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        final StringBuilder stringBuilder = new StringBuilder();
        String s;
        while ((s = reader.readLine())!=null){
            stringBuilder.append(s);
        }

        stream.close();
        reader.close();

        return stringBuilder.toString();
    }

    private class MicrosoftHttpHandler implements HttpHandler {
        private boolean got = false;

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (!got) {
                final String query = httpExchange.getRequestURI().getQuery();
                if (query.contains("code")) {
                    got = true;
                    final String code = query.split("code=")[1];

                    final String[] microsoftTokenAndRefreshToken = getMicrosoftTokenAndRefreshToken(code);
                    final String xBoxLiveToken = getXBoxLiveToken(microsoftTokenAndRefreshToken[0]);
                    final String[] xstsTokenAndHash = getXSTSTokenAndUserHash(xBoxLiveToken);
                    final String accessToken = getAccessToken(xstsTokenAndHash[0], xstsTokenAndHash[1]);
                    final URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", "Bearer " + accessToken);

                    final String read = read(connection.getInputStream());
                    final JSONObject jsonObject = JSON.parseObject(read);
                    final String uuid = jsonObject.getString("id");
                    final String userName = jsonObject.getString("name");

                    MicrosoftLogin.this.uuid = uuid;
                    MicrosoftLogin.this.userName = userName;
                    MicrosoftLogin.this.accessToken = accessToken;
                    MicrosoftLogin.this.refreshToken = microsoftTokenAndRefreshToken[1];
                    MicrosoftLogin.this.logged = true;
                }
            }
        }
    }
}
