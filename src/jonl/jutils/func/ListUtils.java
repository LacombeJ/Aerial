package jonl.jutils.func;

import java.util.ArrayList;
import java.util.Comparator;

public class ListUtils
{

	public static <X,Y> List<Y> map(List<X> list, Function<X,Y> function) {
		return list.map(function);
	}
	
	public static <X,Y> List<Y> map(X[] list, Function<X,Y> function) {
		return list(list).map(function);
	}
	
	public static <X> List<X> filter(List<X> list, Function<X,Boolean> condition) {
		return list.filter(condition);
	}
	
	public static <X> X reduce(List<X> list, Function2D<X,X,X> function) {
		return list.reduce(function);
	}
	
	public static <X> Tuple2<List<X>,List<X>> split(List<X> list, Function<X,Boolean> condition) {
		return list.split(condition);
	}
	
	public static <X> List<X> wrap(X x) {
		List<X> array = new List<>();
		array.add(x);
		return array;
	}
	
	public static <X> X first(List<X> list) {
		return list.first();
	}
	
	public static <X> X last(List<X> list) {
		return list.last();
	}
	
	public static <X> boolean contains(List<X> list, X x) {
		return list.contains(x);
	}
	
	public static <X> boolean contains(List<X> list, Function<X,Boolean> contains) {
		return list.contains(contains);
	}
	
	public static <X> List<X> addFirst(X first, List<X> list) {
		return list.addFirst(first);
	}
	
	public static <X> List<X> addLast(List<X> list, X last) {
		return list.addLast(last);
	}
	
	public static <X,Y> List<Y> enumerate(List<X> list, Function2D<X,Integer,Y> function) {
		return list.enumerate(function);
	}
	
	public static <X,Y> Tuple2<X,Y> tuple(X x, Y y) {
		return new Tuple2<X,Y>(x,y);
	}
	
	public static <X,Y,Z> Tuple3<X,Y,Z> tuple(X x, Y y, Z z) {
		return new Tuple3<X,Y,Z>(x,y,z);
	}
	
	public static <X> List<List<X>> divide(List<X> list, Function2D<X,X,Boolean> condition) {
		return list.divide(condition);
	}
	
	
	public static <X> List<List<X>> bin(List<X> list, Function2D<X,X,Boolean> unique) {
		return null;
	}
	
	public static <X> List<List<X>> bin(List<X> list, Function<X,Integer> generator) {
		return list.bin(generator);
	}
	
	
	public static <X> List<X> order(List<X> list, Function2D<X,X,Integer> compare) {
		return list.order(compare);
	}
	
	public static <X> List<X> copy(List<X> list, Function<X,X> copy) {
		return list.copy(copy);
	}
	
	public static <X> List<X> copy(X[] list, Function<X,X> copy) {
		return list(list).copy(copy);
	}
	
	public static <X> List<X> list(ArrayList<X> list) {
		return new List<X>(list);
	}
	
	@SuppressWarnings("unchecked")
	public static <X> List<X> list(X... list) {
		List<X> array = new List<>(list.length);
		for (X x : list) {
			array.add(x);
		}
		return array;
	}
	
	public static List<Integer> listInt(int... list) {
        List<Integer> array = new List<>(list.length);
        for (int x : list) {
            array.add(x);
        }
        return array;
    }
	
    public static List<Float> listFloat(float... list) {
        List<Float> array = new List<>(list.length);
        for (float x : list) {
            array.add(x);
        }
        return array;
    }
    
    public static final class FunctionComparator<X> implements Comparator<X> {
		private final Function2D<X,X,Integer> comparator;
		public FunctionComparator(Function2D<X,X,Integer> compare) {
			comparator = compare;
		}
		@Override
		public int compare(X x, X y) {
			return comparator.f(x, y);
		}
	}	
	
}
