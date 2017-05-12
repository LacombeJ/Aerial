package jonl.jutils.func;

import java.util.ArrayList;
import java.util.Comparator;

import jonl.jutils.misc.TypeUtils;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public class FunctionUtils {

	public static <X,Y> ArrayList<Y> map(Function<X,Y> function, ArrayList<X> list) {
		ArrayList<Y> ret = new ArrayList<>(list.size());
		for (X x : list) {
			ret.add(function.f(x));
		}
		return ret;
	}
	
	public static <X,Y> ArrayList<Y> map(Function<X,Y> function, X[] list) {
		ArrayList<Y> ret = new ArrayList<>(list.length);
		for (X x : list) {
			ret.add(function.f(x));
		}
		return ret;
	}
	
	public static <X> ArrayList<X> filter(Function<X,Boolean> condition, ArrayList<X> list) {
		ArrayList<X> ret = new ArrayList<>();
		for (X x : list) {
			if (condition.f(x)) {
				ret.add(x);
			}
		}
		return ret;
	}
	
	public static <X> X reduce(Function2D<X,X,X> function, ArrayList<X> list) {
		X accum = list.get(0);
		for (int i=1; i<list.size(); i++) {
			accum = function.f(accum, list.get(i));
		}
		return accum;
	}
	
	public static <X> Tuple2<ArrayList<X>,ArrayList<X>> split(Function<X,Boolean> condition, ArrayList<X> list) {
		ArrayList<X> trueGroup = new ArrayList<>();
		ArrayList<X> falseGroup = new ArrayList<>();
		for (X x : list) {
			if (condition.f(x)) {
				trueGroup.add(x);
			} else {
				falseGroup.add(x);
			}
		}
		return new Tuple2<ArrayList<X>,ArrayList<X>>(trueGroup,falseGroup);
	}
	
	public static <X> ArrayList<X> wrap(X x) {
		ArrayList<X> array = new ArrayList<>();
		array.add(x);
		return array;
	}
	
	public static <X> X first(ArrayList<X> list) {
		return list.get(0);
	}
	
	public static <X> X last(ArrayList<X> list) {
		return list.get(list.size()-1);
	}
	
	public static <X> boolean contains(ArrayList<X> list, X x) {
		return list.contains(x);
	}
	
	public static <X> boolean contains(Function<X,Boolean> contains, ArrayList<X> list) {
		for (X x : list) {
			if (contains.f(x)) {
				return true;
			}
		}
		return false;
	}
	
	public static <X> ArrayList<X> concat(X first, ArrayList<X> list) {
		list.add(0, first);
		return list;
	}
	
	public static <X> ArrayList<X> concat(ArrayList<X> list, X last) {
		list.add(last);
		return list;
	}
	
	public static <X,Y> ArrayList<Y> enumerate(Function2D<X,Integer,Y> function, ArrayList<X> list) {
		ArrayList<Y> ret = new ArrayList<>(list.size());
		for (int i=0; i<list.size(); i++) {
			X x = list.get(i);
			ret.add(function.f(x,i));
		}
		return ret;
	}
	
	public static <X,Y> Tuple2<X,Y> tuple(X x, Y y) {
		return new Tuple2<X,Y>(x,y);
	}
	
	public static <X,Y,Z> Tuple3<X,Y,Z> tuple(X x, Y y, Z z) {
		return new Tuple3<X,Y,Z>(x,y,z);
	}
	
	public static <X> ArrayList<ArrayList<X>> divide(Function2D<X,X,Boolean> condition, ArrayList<X> list) {
		return reduce( 
			(x,y) -> {
				if (condition.f(last(last(x)), first(first(y)))) {
					last(x).add(first(first(y)));
				} else {
					x.add(first(y));
				}
				return x;
			},
			map(x -> wrap(wrap(x)) , list));
	}
	
	public static <X> ArrayList<ArrayList<X>> bin(Function2D<X,X,Boolean> unique, ArrayList<X> list) {
		//TODO
		return reduce( 
				(x,y) -> {
					return x;
				},
				map(x -> wrap(wrap(x)) , list));
		//TODO
	}
	
	public static <X> ArrayList<ArrayList<X>> bin(Function<X,Integer> generator, ArrayList<X> list) {
		return map (
			x -> (map(y->((Tuple2<X,Integer>)y).x,x)) , 
			divide( 
				(t0,t1) -> t0.y==t1.y , 
				sort( 
					(t0,t1) -> TypeUtils.compare(t0.y,t1.y) , 
					map( 
						(x) -> tuple(x,generator.f(x)) , 
						list
					) 
				) 
			) 
		);
	}
	
	
	public static <X> ArrayList<X> sort(Function2D<X,X,Integer> compare, ArrayList<X> list) {
		ArrayList<X> sorted = copy(x->x, list);
		sorted.sort(new FunctionComparator<X>(compare));
		return sorted;
	}
	
	public static <X> ArrayList<X> sort(Function2D<X,X,Integer> compare, X[] list) {
		ArrayList<X> sorted = copy(x->x, list);
		sorted.sort(new FunctionComparator<X>(compare));
		return sorted;
	}
	
	public static <X> ArrayList<X> copy(Function<X,X> copy, ArrayList<X> list) {
		return map(copy,list);
	}
	
	public static <X> ArrayList<X> copy(Function<X,X> copy, X[] list) {
		return map(copy,list);
	}
	
	@SuppressWarnings("unchecked")
	public static <X> ArrayList<X> list(X... list) {
		ArrayList<X> array = new ArrayList<>(list.length);
		for (X x : list) {
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
