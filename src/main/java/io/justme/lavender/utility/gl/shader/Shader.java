package io.justme.lavender.utility.gl.shader;

import io.justme.lavender.utility.gl.OGLUtility;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author JustMe.
 * @since 2024/1/7
 **/
@SuppressWarnings("all")
public class Shader {

    protected Minecraft mc = Minecraft.getMinecraft();
    private final int programID, fragmentShaderID, vertexShaderID;
    protected ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

    @SneakyThrows
    private Shader(String fragmentShader, String vertexShader){
        int program = glCreateProgram();

        var vertexSource = readShader(vertexShader);
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexSource);
        glCompileShader(vertexShaderID);

        var fragmentSource = readShader(fragmentShader);
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSource);
        glCompileShader(fragmentShaderID);

        glAttachShader(program, fragmentShaderID);
        glAttachShader(program, vertexShaderID);

        glLinkProgram(program);

        this.programID = program;
    }

    public Shader(String fragmentShader) {
        this(fragmentShader, "vertex.vsh");
    }

    @SneakyThrows
    private static String readShader(String fileName) {
        final StringBuilder stringBuilder = new StringBuilder();

        final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(Shader.class.getClassLoader().getResourceAsStream(String.format("assets/minecraft/la/shaders/%s", fileName))));
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line).append('\n');
        bufferedReader.close();
        inputStreamReader.close();

        return stringBuilder.toString();
    }

    public void set() {
        glUseProgram(programID);
    }

    public void reset() {
        glUseProgram(GL_NONE);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(programID, name);
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(programID, name);
        switch (args.length) {
            case 1:
                glUniform1f(loc, args[0]);
                break;
            case 2:
                glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }


}
