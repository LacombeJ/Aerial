package ax.commons.io;

import java.util.ArrayList;

import ax.commons.func.List;
import ax.commons.misc.StringUtils;
import ax.commons.structs.Array2D;

public class PrintChart extends Array2D<String> {
    
    final boolean[] rowHasBorder;
    final boolean[] colHasBorder;
    
    final char[] rowBorder;
    final String[] colBorder;
    
    public PrintChart(int rows, int columns) {
        super(List.comprehension(rows*columns,i -> "").toArray(new String[0]), rows);
        rowHasBorder = new boolean[rows+1];
        colHasBorder = new boolean[columns+1];
        rowBorder = new char[rows+1];
        colBorder = new String[columns+1];
    }
    
    public void setRowBorder(int row, char border) {
        rowHasBorder[row] = true;
        rowBorder[row] = border;
    }
    
    public void setColumnBorder(int column, String border) {
        colHasBorder[column] = true;
        colBorder[column] = border;
    }
    
    private int getColBorderTotal() {
        int sum = 0;
        for (int i=0; i<colHasBorder.length; i++) {
            if (colHasBorder[i]) {
                sum += colBorder[i].length() + 1;
            }
        }
        return sum - 1;
    }
    
    private void checkPrintRowBorder(int row, int rowLen) {
        if (rowHasBorder[row]) {
            System.out.println(StringUtils.repeat(rowBorder[row]+"", rowLen));
        }
    }
    
    private void checkPrintColBorder(int col) {
        if (colHasBorder[col]) {
            System.out.print(colBorder[col]+" ");
        }
    }
    
    public void print() {
        ArrayList<Integer> maxLenColumns =
                List.comprehension(
                    getColumns(),
                    j -> List.accumulate(
                    		List.comprehension( getRows(), i->get(i,j) ),
                    		(x,y) -> (x.length()>y) ? x.length() : y, 0 )
                );
        int rowLen = List.accumulate(maxLenColumns, (x,y) -> x + y + 1, 0) + getColBorderTotal();
        for (int i=0; i<getRows(); i++) {
            checkPrintRowBorder(i,rowLen);
            for (int j=0; j<getColumns(); j++) {
                checkPrintColBorder(j);
                System.out.print(StringUtils.fit(get(i,j), maxLenColumns.get(j)+1));
            }
            checkPrintColBorder(colHasBorder.length-1);
            System.out.println();
        }
        checkPrintRowBorder(rowHasBorder.length-1,rowLen);
    }
    
}
