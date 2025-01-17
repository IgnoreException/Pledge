package dev.thomazz.pledge.packet.providers;

import dev.thomazz.pledge.packet.PacketProvider;
import dev.thomazz.pledge.util.MinecraftReflection;
import dev.thomazz.pledge.util.ReflectionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class TransactionPacketProvider implements PacketProvider {
    private final Class<?> inTransactionClass;
    private final Class<?> outTransactionClass;
    private final Class<?> loginClass;
    private final Class<?> keepAliveClass;
    private final Class<?> disconnectClass;

    private final Field inTransactionIdField;
    private final Constructor<?> outTransactionConstructor;

    public TransactionPacketProvider() throws Exception {
        this.inTransactionClass = MinecraftReflection.gamePacket("PacketPlayInTransaction");
        this.outTransactionClass = MinecraftReflection.gamePacket("PacketPlayOutTransaction");
        this.loginClass = MinecraftReflection.gamePacket("PacketPlayOutLogin");
        this.keepAliveClass = MinecraftReflection.gamePacket("PacketPlayOutKeepAlive");
        this.disconnectClass = MinecraftReflection.gamePacket("PacketPlayOutKickDisconnect");

        this.inTransactionIdField = ReflectionUtil.getFieldByType(this.inTransactionClass, short.class);
        this.outTransactionConstructor = this.outTransactionClass.getConstructor(int.class, short.class, boolean.class);
    }

    @Override
    public Object buildPacket(int id) throws Exception {
        return this.outTransactionConstructor.newInstance(0, (short) id, false);
    }

    @Override
    public Integer idFromPacket(Object packet) throws Exception {
        if (this.inTransactionClass.isInstance(packet)) {
            return (int) this.inTransactionIdField.getShort(packet);
        }

        return null;
    }

    @Override
    public boolean isLogin(Object packet) {
        return this.loginClass.isInstance(packet);
    }

    @Override
    public boolean isKeepAlive(Object packet) {
        return this.keepAliveClass.isInstance(packet);
    }

    @Override
    public boolean isDisconnect(Object packet) {
        return this.disconnectClass.isInstance(packet);
    }

    @Override
    public int getLowerBound() {
        return Short.MIN_VALUE;
    }

    @Override
    public int getUpperBound() {
        return -1;
    }
}
