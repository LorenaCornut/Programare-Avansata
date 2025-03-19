public class Pair<T, U> {
    private T first;
    private U second;
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    public T getFirst()
    {
        return first;
    }
    public U getSecond()
    {
        return second;
    }
    public void setFirst(T first)
    {
        this.first = first;
    }
    public void setSecond(U second)
    {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || getClass() != obj.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        if(first != null ? !first.equals(pair.first) : pair.first != null) return false;
        return second != null ? second.equals(pair.second) : pair.second == null;
    }
}
















































