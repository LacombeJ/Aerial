package jonl.jutils.func;

import jonl.jutils.io.PrintChart;
import jonl.jutils.structs.Array2D;
import jonl.jutils.structs.BijectiveMap;

public class MatrixChart<X,Y,Z>
{
	
    private final BijectiveMap<X,Integer> rowMap;
    private final BijectiveMap<Y,Integer> colMap;
    private final Array2D<Z> matrix;
    
    public MatrixChart(List<X> row, List<Y> col, Function2D<X,Y,Z> f) {
        rowMap = new BijectiveMap<>(row.size());
        colMap = new BijectiveMap<>(col.size());
        for (int i=0; i<row.size(); i++) {
            rowMap.put(row.get(i), i);
        }
        for (int j=0; j<col.size(); j++) {
            colMap.put(col.get(j), j);
        }
        matrix = new Array2D<>(rowMap.size(),colMap.size());
        for (int i=0; i<row.size(); i++) {
            for (int j=0; j<col.size(); j++) {
                matrix.set(i, j, f.f(rowMap.getKey(i), colMap.getKey(j)));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public MatrixChart(List<X> list, Function2D<X,X,Z> f) {
        this(list, (List<Y>) list, (x,y) -> f.f(x,(X) y) );
    }
    
    
    
    public Z get(X row, Y col) {
        int i = rowMap.get(row);
        int j = colMap.get(col);
        return matrix.get(i, j);
    }
    
    public void set(X row, Y col, Z value) {
        int i = rowMap.get(row);
        int j = colMap.get(col);
        matrix.set(i, j, value);
    }
    
    
    

    public void printChart() {
        PrintChart pc = new PrintChart(rowMap.size()+1,colMap.size()+1);
        
        for (int i=0; i<pc.getRows(); i++) {
            for (int j=0; j<pc.getColumns(); j++) {
                if (i>0 && j>0) {
                    Z z = matrix.get(i-1, j-1);
                    pc.set(i, j, z.toString());
                }
                if (i==0 && j>0) {
                    pc.set(i, j, colMap.getKey(j-1).toString());
                }
                if (j==0 && i>0) {
                    pc.set(i, j, rowMap.getKey(i-1).toString());
                }
            }
        }
        
        pc.setRowBorder(1, '-');
        pc.setColumnBorder(1, "|");
        pc.print();
    }
    
    
    
    
    
	
}
