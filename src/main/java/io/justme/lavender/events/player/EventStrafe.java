package io.justme.lavender.events.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.lenni0451.asmevents.event.IEvent;
import net.lenni0451.asmevents.event.types.ICancellableEvent;
import net.minecraft.client.Minecraft;

@Getter
@Setter
public class EventStrafe implements IEvent , ICancellableEvent {

    private float forward;
    private float strafe;
    private float friction;
    private float yaw;

    private boolean cancel;

    public EventStrafe(float forward, float strafe, float friction, float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.yaw = yaw;
    }

    public void setSpeed(final double speed, final double motionMultiplier) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        Minecraft.getMinecraft().thePlayer.motionX *= motionMultiplier;
        Minecraft.getMinecraft().thePlayer.motionZ *= motionMultiplier;
    }

    public void setSpeed(final double speed) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        Minecraft.getMinecraft().thePlayer.motionX = 0;
        Minecraft.getMinecraft().thePlayer.motionZ = 0;
    }

    @Override
    public boolean isCancelled() {
        return isCancel();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        setCancel(cancelled);
    }
}
