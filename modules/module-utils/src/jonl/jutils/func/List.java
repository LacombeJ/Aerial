package jonl.jutils.func;

import java.util.ArrayList;
import java.util.Comparator;

import jonl.jutils.misc.TypeUtils;

public class List
{
	
	@SuppressWarnings("unchecked")
	public static <X> ArrayList<X> list(X... list) {
		ArrayList<X> ret = new ArrayList<>();
		for (X x : list) {
			ret.add(x);
		}
		return ret;
	}
	
	public static <X> ArrayList<X> list(java.util.List<X> list) {
		ArrayList<X> ret = new ArrayList<>();
		ret.addAll(list);
		return ret;
	}
	
	public static <X> ArrayList<X> wrap(X x) {
		ArrayList<X> ret = new ArrayList<>(1);
		ret.add(x);
		return ret;
	}
	
	public static <X,Y> ArrayList<Y> map(java.util.List<X> list, Function<X,Y> function) {
		ArrayList<Y> ret = new ArrayList<>(list.size());
		for (X x : list) {
			ret.add(function.f(x));
		}
		return ret;
	}
	
	public static <X> ArrayList<X> comprehension(int size, Function<Integer,X> comprehension) {
		ArrayList<X> ret = new ArrayList<>(size);
		for (int i=0; i<size; i++) {
			ret.add(comprehension.f(i));
		}
		return ret;
	}
	
	
	
	public static <X> ArrayList<X> filter(java.util.List<X> list, Function<X,Boolean> condition) {
		ArrayList<X> ret = new ArrayList<>();
		for (X x : list) {
			if (condition.f(x)) {
				ret.add(x);
			}
		}
		return ret;
	}
	
	public static <X> ArrayList<X> repeat(java.util.List<X> list, int n) {
	    ArrayList<X> ret = new ArrayList<>();
	    for (int i=0; i<n; i++) {
	        ret.addAll(list);
	    }
	    return ret;
	}
	
	public static <X,Y> Y accumulate(java.util.List<X> list, Function2D<X,Y,Y> function, Y y) {
	    if (list.size()==0) return null;
	    Y accum = y;
	    for (int i=0; i<list.size(); i++) {
	        accum = function.f(list.get(i), accum);
	    }
	    return accum;
	}
	
	public static <X> X reduce(java.util.List<X> list, Function2D<X,X,X> function) {
	    if (list.size()==0) return null;
		X accum = list.get(0);
		for (int i=1; i<list.size(); i++) {
			accum = function.f(accum, list.get(i));
		}
		return accum;
	}

	public static <X> Tuple2<ArrayList<X>,ArrayList<X>> split(java.util.List<X> list, Function<X,Boolean> condition) {
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
	
	public static <X,Y> ArrayList<Y> gather(java.util.List<X> list, Function<X,java.util.List<Y>> function) {
		ArrayList<Y> ret = new ArrayList<>();
		for (X x : list) {
			ret.addAll(function.f(x));
		}
		return ret;
	}
	
	public static <X> void iterate(java.util.List<X> list, Callback<X> callback) {
		for (X x : list) {
			callback.f(x);
		}
	}
	
	public static <X> X first(java.util.List<X> list) {
		return list.get(0);
	}
	
    public static <X> X head(java.util.List<X> list) {
        return list.get(0);
    }
	
	public static <X> X last(java.util.List<X> list) {
		return list.get(list.size()-1);
	}
	
	public static <X> ArrayList<X> first(java.util.List<X> list, int n) {
        return sub(list,0,n);
    }
    
	public static <X> ArrayList<X> last(java.util.List<X> list, int n) {
        return sub(list, list.size()-n, list.size());
    }
	
    public static <X> ArrayList<X> tail(java.util.List<X> list) {
        return sub(list, 1, list.size());
    }
	
	public static <X> void setFirst(java.util.List<X> list, X x) {
        list.set(0, x);
    }
    
    public static <X> void setFirst(java.util.List<X> list, java.util.List<X> x) {
        int addLen = x.size();
        for (int i=0; i<addLen; i++) {
            list.set(i,x.get(i));
        }
    }

    public static <X> void setLast(java.util.List<X> list, X x) {
        list.set(list.size()-1, x);
    }
    
    public static <X> void setLast(java.util.List<X> list, java.util.List<X> x) {
        int listLen = list.size();
        int addLen = x.size();
        for (int i=listLen-addLen, j=0; i<listLen; i++, j++) {
            list.set(i,x.get(j));
        }
    }
    
    public static <X> ArrayList<X> append(java.util.List<X> listA, java.util.List<X> listB) {
        ArrayList<X> ret = List.list(listA);
        ret.addAll(listB);
        return ret;
    }
    
    public static <X> ArrayList<X> prepend(java.util.List<X> listA, java.util.List<X> listB) {
        return append(listB,listA);
    }
	
    public static <X> ArrayList<X> sub(java.util.List<X> list, int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, list.size());
        ArrayList<X> ret = new ArrayList<X>();
        for (int i=fromIndex; i<toIndex; i++) {
            ret.add(list.get(i));
        }
        return ret;
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
	
	public static <X> boolean contains(java.util.List<X> list, Function<X,Boolean> contains) {
		for (X x : list) {
			if (contains.f(x)) {
				return true;
			}
		}
		return false;
	}
	
	public static <X> ArrayList<X> addFirst(java.util.List<X> list, X first) {
		ArrayList<X> concat = new ArrayList<>();
		concat.add(first);
		concat.addAll(list);
		return concat;
	}
	
	public static <X> ArrayList<X> addLast(java.util.List<X> list, X last) {
		ArrayList<X> concat = new ArrayList<>();
		concat.addAll(list);
		concat.add(last);
		return concat;
	}
	
	public static <X,Y> ArrayList<Y> enumerate(java.util.List<X> list, Function2D<X,Integer,Y> function) {
		ArrayList<Y> ret = new ArrayList<>(list.size());
		for (int i=0; i<list.size(); i++) {
			X x = list.get(i);
			ret.add(function.f(x,i));
		}
		return ret;
	}
	
	public static <X> ArrayList<ArrayList<X>> divide(java.util.List<X> list, Function2D<X,X,Boolean> condition) {
		
		ArrayList<ArrayList<X>> map = reduce(
			map(list, x -> wrap(wrap(x))) ,
			(x,y) -> {
				if (condition.f(last(last(x)), first(first(y)))) {
					last(x).add(first(first(y)));
				} else {
					x.add(first(y));
				}
				return x;
			}
		);
		return map==null ? new ArrayList<ArrayList<X>>() : map;
	}
	
	public static <X> ArrayList<X> copy(java.util.List<X> list) {
		return map(list, x -> x);
	}
	
	public static <X> ArrayList<X> copy(java.util.List<X> list, Function<X,X> copy) {
		return map(list, copy);
	}
	
	public static <X> ArrayList<X> order(java.util.List<X> list, Function2D<X,X,Integer> compare) {
		ArrayList<X> sorted = copy(list, x->x);
		sorted.sort(new FunctionComparator<X>(compare));
		return sorted;
	}
	
	public static <X> ArrayList<X> sort(java.util.List<X> list, Function2D<X,X,Integer> compare) {
        return order(list, compare);
    }
	
	public static <X> ArrayList<ArrayList<X>> bin(java.util.List<X> list, Function<X,Integer> generator) {
		ArrayList<Tuple2<X,Integer>> list0 = map(list, x -> new Tuple2<>(x,generator.f(x)));
		ArrayList<Tuple2<X,Integer>> list1 = order(list0, (t0,t1) -> TypeUtils.compare(t0.y,t1.y));
		ArrayList<ArrayList<Tuple2<X,Integer>>> list2 = divide(list1, (t0,t1) -> t0.y==t1.y);
		ArrayList<ArrayList<X>> list3 = map(list2, x -> map(x, y->y.x));
		return list3;
	}
	
	public static <X> ArrayList<ArrayList<X>> bin(java.util.List<X> list, Function2D<X,X,Boolean> unique) {
	    ArrayList<ArrayList<X>> ret =
    		reduce(
	    		map(list, x -> wrap(wrap(x))),
	            (x,y) -> {
	                ArrayList<X> find = find(x, u -> unique.f(u.get(0),first(first(y))));
	                if (find!=null) {
	                    find.add(first(first(y)));
	                } else {
	                    x.add(first(y));
	                }
	                return x;
	            }
            );
	    return ret==null ? new ArrayList<ArrayList<X>>() : ret;
	}
	
	/**
	 * @return all combinations of elements in this list where order does not matter
	 */
	public static <X> ArrayList<ArrayList<X>> combination(java.util.List<X> list) {
	    if (list.size()==0) {
	        return wrap(new ArrayList<X>());
	    }
	    ArrayList<ArrayList<X>> sub = combination(tail(list));
	    ArrayList<ArrayList<X>> sub2 = List.map(sub, s -> addFirst(s,head(list)));
	    return List.append(sub,sub2);
	}
	
	public static <X> X find(java.util.List<X> list, Function<X,Boolean> function) {
	    for (X x : list) {
	        if (function.f(x)) {
	            return x;
	        }
	    }
	    return null;
	}
	
	public static <X> int index(java.util.List<X> list, Function<X,Boolean> function) {
	    for (int i=0; i<list.size(); i++) {
	        if (function.f(list.get(i))) {
                return i;
            }
	    }
	    return -1;
	}
	
	public static <X> int[] toIntArray(java.util.List<X> list) {
	    int[] array = new int[list.size()];
	    for (int i=0; i<array.length; i++) {
	        array[i] = (int)(Integer) list.get(i);
	    }
	    return array;
	}
	
	public static <X> float[] toFloatArray(java.util.List<X> list) {
        float[] array = new float[list.size()];
        for (int i=0; i<array.length; i++) {
            array[i] = (float)(Float) list.get(i);
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
