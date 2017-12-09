package jonl.jutils.func;

import java.util.ArrayList;
import java.util.Collection;

import jonl.jutils.misc.TypeUtils;

import static jonl.jutils.func.ListUtils.*;

@SuppressWarnings("serial") //TODO ?
public class List<X> extends ArrayList<X>
{
	
	public List() {
		super();
	}

	public List(int size) {
		super(size);
	}
	
	public List(Collection<? extends X> c) {
		super(c);
	}
	
	public List(int size, Function<Integer,X> comprehension) {
	    super(size);
	    for (int i=0; i<size; i++) {
	        add(comprehension.f(i));
	    }
	}
	
	public <Y> List(List<Y> list, Function<Y,List<X>> grow) {
	    super();
	    for (Y y : list) {
	        addAll(grow.f(y));
	    }
	}
	
	
	public ArrayList<X> array() {
		return new ArrayList<X>(this);
	}

	public <Y> List<Y> map(Function<X,Y> function) {
		List<Y> ret = new List<>(size());
		for (X x : this) {
			ret.add(function.f(x));
		}
		return ret;
	}
	
	public List<X> filter(Function<X,Boolean> condition) {
		List<X> ret = new List<>();
		for (X x : this) {
			if (condition.f(x)) {
				ret.add(x);
			}
		}
		return ret;
	}
	
	public List<X> repeat(int n) {
	    List<X> list = new List<>();
	    for (int i=0; i<n; i++) {
	        list.addAll(this);
	    }
	    return list;
	}
	
	public <Y> Y accumulate(Function2D<X,Y,Y> function, Y y) {
	    if (size()==0) return null;
	    Y accum = y;
	    for (int i=0; i<size(); i++) {
	        accum = function.f(get(i), accum);
	    }
	    return accum;
	}
	
	public X reduce(Function2D<X,X,X> function) {
	    if (size()==0) return null;
		X accum = get(0);
		for (int i=1; i<size(); i++) {
			accum = function.f(accum, get(i));
		}
		return accum;
	}

	public Tuple2<List<X>,List<X>> split(Function<X,Boolean> condition) {
		List<X> trueGroup = new List<>();
		List<X> falseGroup = new List<>();
		for (X x : this) {
			if (condition.f(x)) {
				trueGroup.add(x);
			} else {
				falseGroup.add(x);
			}
		}
		return new Tuple2<List<X>,List<X>>(trueGroup,falseGroup);
	}
	
	public X first() {
		return get(0);
	}
	
	public X last() {
		return get(size()-1);
	}
	
	public List<X> first(int n) {
        return sub(0,n);
    }
    
	public List<X> last(int n) {
        return sub(size()-n,size());
    }
	
   public void setFirst(X x) {
        set(0, x);
    }
    
    public void setFirst(List<X> x) {
        int addLen = x.size();
        for (int i=0; i<addLen; i++) {
            set(i,x.get(i));
        }
    }

    public void setLast(X x) {
        set(size()-1, x);
    }
    
    public void setLast(List<X> x) {
        int listLen = size();
        int addLen = x.size();
        for (int i=listLen-addLen, j=0; i<listLen; i++, j++) {
            set(i,x.get(j));
        }
    }
	
    private List<X> sub(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());
        List<X> list = new List<X>();
        for (int i=fromIndex; i<toIndex; i++) {
            list.add(get(i));
        }
        return list;
    }
	
	@Override
	public List<X> subList(int fromIndex, int toIndex) {
        return sub(fromIndex,toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }
	
	public boolean contains(Function<X,Boolean> contains) {
		for (X x : this) {
			if (contains.f(x)) {
				return true;
			}
		}
		return false;
	}
	
	public List<X> addFirst(X first) {
		List<X> concat = new List<>();
		concat.add(first);
		concat.addAll(this);
		return concat;
	}
	
	public List<X> addLast(X last) {
		List<X> concat = new List<>();
		concat.addAll(this);
		concat.add(last);
		return concat;
	}
	
	public <Y> List<Y> enumerate(Function2D<X,Integer,Y> function) {
		List<Y> ret = new List<>(size());
		for (int i=0; i<size(); i++) {
			X x = get(i);
			ret.add(function.f(x,i));
		}
		return ret;
	}
	
	public List<List<X>> divide(Function2D<X,X,Boolean> condition) {
		List<List<X>> map = map(x -> wrap(wrap(x))).reduce(
			(x,y) -> {
				if (condition.f(x.last().last(), y.first().first())) {
					x.last().add(y.first().first());
				} else {
					x.add(y.first());
				}
				return x;
		});
		return map==null ? new List<List<X>>() : map;
	}
	
	public List<X> copy() {
		return map(x -> x);
	}
	
	public List<X> copy(Function<X,X> copy) {
		return map(copy);
	}
	
	public List<X> order(Function2D<X,X,Integer> compare) {
		List<X> sorted = copy(x->x);
		sorted.sort(new FunctionComparator<X>(compare));
		return sorted;
	}
	
	public List<List<X>> bin(Function<X,Integer> generator) {
		 return map(x -> tuple(x,generator.f(x)))
				.order((t0,t1) -> TypeUtils.compare(t0.y,t1.y))
				.divide((t0,t1) -> t0.y==t1.y)
				.map(x -> x.map(y->y.x));
	}
	
	public List<List<X>> bin(Function2D<X,X,Boolean> unique) {
	    List<List<X>> map = map(x -> wrap(wrap(x))).reduce(
	            (x,y) -> {
	                List<X> find = x.find(u -> unique.f(u.get(0),y.first().first()));
	                if (find!=null) {
	                    find.add(y.first().first());
	                } else {
	                    x.add(y.first());
	                }
	                return x;
	        });
	    return map==null ? new List<List<X>>() : map;
	}
	
	public X find(Function<X,Boolean> function) {
	    for (X x : this) {
	        if (function.f(x)) {
	            return x;
	        }
	    }
	    return null;
	}
	
	public int index(Function<X,Boolean> function) {
	    for (int i=0; i<size(); i++) {
	        if (function.f(get(i))) {
                return i;
            }
	    }
	    return -1;
	}
	
	public int[] toIntArray() {
	    int[] array = new int[size()];
	    for (int i=0; i<array.length; i++) {
	        array[i] = (int) get(i);
	    }
	    return array;
	}
	
	public float[] toFloatArray() {
        float[] array = new float[size()];
        for (int i=0; i<array.length; i++) {
            array[i] = (float) get(i);
        }
        return array;
    }
	
}
