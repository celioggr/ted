package TextEditor;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.ACS;
import com.googlecode.lanterna.terminal.Terminal;

public class BufferView {

    private static Terminal term;
    private FileBuffer buf;
    private FileBuffer buf1;
    private FileBuffer buf2;
    private int bufferToUse=0;
    private int firstLine =0;
    private int fixedCol=0;

    public BufferView() {

        term = TerminalFacade.createTerminal();
        term.setCursorVisible(true);
        term.enterPrivateMode();
        this.buf = new FileBuffer();
        this.buf1 = new FileBuffer();
        this.buf2 = new FileBuffer();

        do {
            Key k = readInput();
            handleKey(k);
        }while(true);

    }

    /**
     * Reads the pressed key
     * @return key pressed
     */
    public Key readInput() {
        Key r = null;
        while (r == null) {
            r = term.readInput();
        }
        return r;
    }


    /**
     * Handle press keys
     * @param k key pressed
     */
    public void handleKey(Key k) {
    	switch (k.getKind()) {

            case Tab:
                usingTab();
                break;
            case NormalKey:
                normalKey(k);
                break;
            case Backspace:
                buf.deleteChar();
                term.clearScreen();
                break;
            case ArrowLeft:
                buf.moveLeft();
                break;
            case ArrowRight:
                buf.moveRight();
                break;
            case ArrowUp:
                buf.moveUp();

                break;
            case ArrowDown:
                buf.moveDown();
                break;
            case Enter:
                buf.insert('\n');
                term.clearScreen();
                break;
        }
        dealWithCursor();
        refresh();
    }

    /**
     * Switching between buffers
     */
    private void usingTab()
    {
        if(bufferToUse==0)
        {
            buf=buf2;
            bufferToUse=1;
        }
        else if(bufferToUse==1)
        {
            buf=buf1;
            bufferToUse=0;
        }
        term.clearScreen();
    }

    /**
     * Inserts a normal key in buffer and prints it into the terminal
     * @param k key pressed
     */
    private void normalKey(Key k) {
        buf.insert(k.getCharacter());
        term.putCharacter(k.getCharacter());
    }

    /**
     * Sets the limits of the cursor in the terminal
     */
    private void dealWithCursor(){
    	
        if(buf.getCursorLine()< firstLine) {
            term.clearScreen();
            firstLine--;
        }

        //limite da barra de stats
        if(buf.getCursorLine()>= firstLine +getTermHeight()-2) {
            term.clearScreen();
            firstLine++;
            //System.out.println(firstLine);
        }

        if(buf.getCursorColumn() < fixedCol) {
            term.clearScreen();
            fixedCol--;
        }

        if(buf.getCursorColumn() >= fixedCol + getTermWidth()) {
            term.clearScreen();
            fixedCol++;
        }
    }

    /**
     * Updates the visual terminal
     */
    private void refresh() {
        for(int ln = firstLine; ln < buf.getSize() && ln < firstLine+getTermHeight(); ln++) {
            term.moveCursor(0, ln - firstLine);
            StringBuilder line = buf.getI(ln);

            int i = fixedCol;

            while(i < line.length() && i < fixedCol + getTermWidth()){
                term.putCharacter(line.charAt(i));
                i++;
            }
        }
        printLineNumber();
        term.moveCursor(buf.getCursorColumn() - fixedCol,buf.getCursorLine() - firstLine);
        term.flush();
    }

    /**
     * Prints the info of the terminal
     * Current line and Column
     * Current working buffer
     */
    private void printLineNumber(){
    	
        for(int i=0;i<getTermWidth();i++){
            term.moveCursor(i,getTermHeight()-2);
            term.putCharacter(ACS.DOUBLE_LINE_HORIZONTAL);
        }
        term.moveCursor(0,getTermHeight()-1);
        String s = Integer.toString(buf.getCursorLine());
        term.putCharacter('L');
        for(int i=0;i<s.length();i++) term.putCharacter(s.charAt(i));
        String ss = Integer.toString(buf.getCursorColumn());
        term.putCharacter(' ');
        term.putCharacter('C');
        for(int i=0;i<ss.length();i++) term.putCharacter(ss.charAt(i));
        String sss = "            Using buffer " + (bufferToUse+1) +". Press TAB to change";
        for(int i=0;i<sss.length();i++) term.putCharacter(sss.charAt(i));
    }

    /**
     * Returns the terminal Width
     * @return width of the terminal
     */
    private int getTermWidth() {
        return term.getTerminalSize().getColumns();
    }

    /**
     * Returns the terminal Height
     * @return height of the terminal
     */
    private int getTermHeight() {
        return term.getTerminalSize().getRows();
    }

}