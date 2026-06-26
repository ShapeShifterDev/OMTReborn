package omtreborn.turret;

import net.minecraft.network.FriendlyByteBuf;
import omtreborn.tileentity.turrets.AbstractDirectedTurretBlockEntity;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;

public class TurretHeadSettings {

    public float yaw = 0;
    public float pitch = 0;
    public boolean forceFire;
    private boolean isDirected = false;

    public TurretHeadSettings() {}

    public TurretHeadSettings(float yaw, float pitch, boolean forceFire) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.forceFire = forceFire;
    }

    public TurretHeadSettings(TurretHeadBlockEntity turretHead) {
        if (turretHead instanceof AbstractDirectedTurretBlockEntity directed) {
            this.yaw = directed.getYaw();
            this.pitch = directed.getPitch();
            this.isDirected = true;
        }
        this.forceFire = turretHead.getAutoFire();
    }

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
        buf.writeBoolean(forceFire);
    }

    public TurretHeadSettings readFromBuf(FriendlyByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.forceFire = buf.readBoolean();
        return this;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }
}
