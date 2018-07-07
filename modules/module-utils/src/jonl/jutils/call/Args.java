package jonl.jutils.call;

import java.util.ArrayList;

import jonl.jutils.func.Function;

public class Args {

    private final ArrayList<String> args;
    
    public Args(String args) {
        this.args = Parser.args(args);
    }
    
    public int count() {
        return args.size();
    }
    
    public String get(int i) {
        return args.get(i);
    }
    
    public <T> T get(int i, Function<String,T> cast) {
        return cast.f(get(i));
    }
    
    public Args getParsed(int i) {
        return new Args(get(i));
    }
    
    public boolean getBoolean(int i) {
        return Boolean.parseBoolean(get(i));
    }
    
    public byte getByte(int i) {
        return Byte.parseByte(get(i));
    }
    
    public char getChar(int i) {
        return get(i).charAt(0);
    }
    
    public short getShort(int i) {
        return Short.parseShort(get(i));
    }
    
    public int getInt(int i) {
        return Integer.parseInt(get(i));
    }
    
    public long getLong(int i) {
        return Long.parseLong(get(i));
    }
    
    public float getFloat(int i) {
        return Float.parseFloat(get(i));
    }
    
    public double getDouble(int i) {
        return Double.parseDouble(get(i));
    }
    
    public String getString(int i) {
        return get(i);
    }
    
}
