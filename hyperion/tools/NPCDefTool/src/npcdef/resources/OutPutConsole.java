/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npcdef.resources;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.PrintStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author phil
 */
public class OutPutConsole extends PrintStream{

    	private JTextArea jText;
		private JScrollPane scroller;

		public OutPutConsole(JTextArea jText, JScrollPane scroller) {
			super(System.out);
			this.jText = jText;
			this.scroller = scroller;
		}
                @Override
		public void println(Object o) {
			printlnObject(o);
		}

		public void printlnObject(Object o) {
			jText.append((new StringBuilder()).append(o.toString()).append("\n").toString());
			adjustScroller();
		}
                @Override
		public void println(String s) {
			printlnObject(s);
		}
                @Override
		public void println() {
			println("println\n");
		}
                @Override
		public void print(Object o) {
			printObject(o);
		}

		public void printObject(Object o) {
			jText.append(o.toString());
		}
                @Override
		public void print(String s) {
			printObject(s);
		}

		public void adjustScroller() {
			scroller.getVerticalScrollBar().setValue(scroller.getVerticalScrollBar().getMaximum());
		}

}
