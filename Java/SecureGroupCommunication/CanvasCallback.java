package SecureGroupCommunication ;
import SecureGroupCommunication.MessageReceived;

import java.util.StringTokenizer;

// A group object uses an instance of this class to relay
// received messages.
public class CanvasCallback implements MessageReceived {

    private DrawCanvas canvas;
    private GroupWhiteBoard board;

    // Constructor.
    public CanvasCallback(DrawCanvas c, GroupWhiteBoard parent) {
        canvas = c;
        board = parent;
    }

    // Interface method implementation.
    public void messageReceived( String message ) {

        // Tokenize the message.
        StringTokenizer tokens = new StringTokenizer(message);
        String type = tokens.nextToken();

        if(type.equals("ERASE")) {
            int e1 = Integer.parseInt(tokens.nextToken());
            int e2 = Integer.parseInt(tokens.nextToken());
            canvas.remoteErase(e1, e2);
        }
        else if(type.equals("LINE")) {
            int a = Integer.parseInt(tokens.nextToken());
            int b = Integer.parseInt(tokens.nextToken());
            int c = Integer.parseInt(tokens.nextToken());
            int d = Integer.parseInt(tokens.nextToken());
            int color = Integer.parseInt(tokens.nextToken());
            canvas.remoteLine(a, b, c, d, color);
        }
        else if(type.equals("MESSAGE")) {
            String from = tokens.nextToken();
            String data = new String("");
            while(tokens.hasMoreTokens())
                data = data + " " + tokens.nextToken();
            board.displayMessage(from + ": " + data);
        }
    }
}
