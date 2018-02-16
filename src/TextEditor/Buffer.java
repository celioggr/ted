package TextEditor;

import java.util.ArrayList;
import java.util.LinkedList;

public class Buffer
{                            
    private Position cursor;
    private ArrayList<StringBuilder> lineList = new ArrayList<StringBuilder>();
    private LinkedList<Buffer> undos = new LinkedList<Buffer>();
    private StringBuilder clipBoard;
    private int markRow, markCol;

    public class InvalidCharException extends RuntimeException {

    }

    public class InvalidLineException extends RuntimeException {

    }

    public class InvalidColumnException extends RuntimeException {
        //TO DO
    }

    public Buffer() {
        cursor = new Position(0,0);
        StringBuilder s = new StringBuilder("");
        lineList.add(s);
    }

    /**
     * Get the Line at index
     * @param index line number
     * @return String a line
     */
    public String getLine(int index) {
        return lineList.get(index).toString();
    }

    /**
     * Insert a Char at the Cursor position
     * @param c	a Char
     */
    public void insert(char c)
    {
        if( c != '\n' ){
            lineList.get(cursor.x).insert(cursor.y, c);
            cursor.y++;
        }
        else{
            insertLn();
            cursor.y=0;
        }
    }

    /**
     * Break the line at the cursor position
     */
    public void insertLn() {
        StringBuilder line = lineList.get(cursor.x);
        lineList.add(cursor.x+1, new StringBuilder(line.substring( cursor.y , line.length() ) ) );
        lineList.get(cursor.x).delete(cursor.y,line.length()); // apagar a quebra da linha anterior
        cursor.x++;
        cursor.y = lineList.get(cursor.x).length();
    }

    /**
     * Insert a string at the current position
     * @param str a string to add
     */
    public void insertString(String str) throws InvalidCharException {
        if (str.contains("\n"))
            throw new InvalidCharException();
        else {
            StringBuilder line = lineList.get(cursor.x);
            line.insert(cursor.y, str);
            cursor.y += str.length();
        }
    }

    /**
     * Delete the char preceding the current cursor position
     */
    public void deleteChar() {
        if(cursor.y == 0 && cursor.x == 0)
            return;

        StringBuilder line = lineList.get(cursor.x);
        if(cursor.y == 0){ //apagar uma linha em que o cursor-col = 0
            StringBuilder prevLine = lineList.get(cursor.x-1);
            cursor.y = prevLine.length();
            prevLine.append(line); //cola a linha onde houve o del no 1pos à linha anterior
            lineList.remove(cursor.x);
            cursor.x--;
        }
        else{
            lineList.get(cursor.x).deleteCharAt(cursor.y-1);
            cursor.y--;
        }
    }

    /**
    * Move the cursor to the previous (logical) line
    */
    public void moveUp() {
        if(cursor.x != 0)
            cursor.x--;

        // se o cursor está à frente do ultimo char da linha acima conserva esse posição no cursor.y
       if (lineList.get(cursor.x).length() < cursor.y)
            cursor.y = lineList.get(cursor.x).length();
        else {
            if(cursor.x > 0)
            cursor.y = lineList.get(cursor.x-1).length();
        }
    }

    /**
     * Move the cursor to the next (logical) line
     */
    public void moveDown() {
        if(cursor.x != lineList.size()-1)
            cursor.x++;
        else
            cursor.y = lineList.get(cursor.x).length();

        //se a linha abaixo tiver um comp maior que a pos actual do cursor
        if (lineList.get(cursor.x).length() < cursor.y)
            cursor.y = lineList.get(cursor.x).length();
        else{
            if(cursor.x != lineList.size()-1)
                cursor.y = lineList.get(cursor.x).length();
        }

    }

    /**
     * Move the cursor (cursor.y) one position to the left
     */
    public void moveLeft() {
        if(cursor.y == 0 && cursor.x == 0)
            return ;

        if(cursor.y == 0){
            cursor.x --;
            cursor.y = lineList.get(cursor.x).length();
        }
            else
                cursor.y--;
    }

    /**
     * Move the cursor (cursor.y) one position to the right
     */
    public void moveRight(){
        if (cursor.x == lineList.size()-1 && cursor.y == lineList.get(cursor.x).length())
            return;

        if(lineList.get(cursor.x).length() == cursor.y){
            cursor.x++;
            cursor.y = 0;
        }
            else
                cursor.y++;
    }

    /**
     * Creates a new Line by breaking it (current line) at the cursor position
     */
    public void newLine(){
        lineList.add(cursor.x+1,new StringBuilder());
        cursor.x++;
        cursor.y=0;
    }

    /**
     * Returns the size of the lineList.
     */
    public int getSize()
    {
        return this.lineList.size();
    }

    /**
     * Returns the cursor line
     */
    public int getCursorLine()
    {
           return cursor.x;
    }

    /**
     * Returns the cursor column
     */
    public int getCursorColumn()
    {
        return cursor.y;
    }


    /**
     * Sets the cursor.x to a position in choice ||
     *
     */
    public void setCursorLineTo(int i) throws InvalidLineException
    {
       if(this.getSize()<=i) throw new InvalidLineException();
        cursor.x=i;
    }

    /**
     * Sets the cursor.y to a position in choice ||
     */
    public void setCursorColumnTo(int i) {
        if(this.getI(cursor.x).length()<i) throw new InvalidColumnException();
        cursor.y=i;
    }

    /**
     * Returns the i-nesim line
     */
    public StringBuilder getI(int i) throws InvalidLineException
    {
        if(i>=this.getSize()) throw new InvalidLineException();
        return this.lineList.get(i);
    }


    /**
     * Changes start mark position to
     * @param markRow   the line number
     * @param markCol   the column number
     */
    public void setMark(int markRow, int markCol){
        this.markRow = markRow;
        this.markCol = markCol;
    }

    /**
     * Unset the start mark position
     */
    public void unsetMark(){
        this.markRow = -1;
        this.markCol = -1;
    }

    /**
     * Returns the start mark line position
     * @return start mark line position
     */
    public int getMarkRow(){ return this.markRow; }

    /**
     * Returns the start mark column position
     * @return start mark column position
     */
    public int getMarkCol(){ return this.markCol; }

    /**
     * Returns the linelist of buffer
     */
    public ArrayList<StringBuilder> getList() {
        return this.lineList;
    }

    /**
     *  Handle undo
     */
    public void undo() {

    }

    /**
     * return clipboard
     * @return clipboard
     */
    public String getClipBoard(){
        return clipBoard.toString();
    }

}


