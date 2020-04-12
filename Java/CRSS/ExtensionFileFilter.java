/**
 * This class will serve as a filter for our JFileChooser
 *
 * @version     $Id: ExtensionFileFilter.java,v 1.3 2000/05/08 03:54:04 p361-45a Exp $
 *
 * @author      Eric Ferguson
 *
 * Revisions
 *      $Log: ExtensionFileFilter.java,v $
 *      Revision 1.3  2000/05/08 03:54:04  p361-45a
 *      fixed formatting
 *
 *      Revision 1.2  2000/04/26 17:16:28  etf2954
 *      Added the two deferred methods
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.File;

public class ExtensionFileFilter extends FileFilter {

    String[] extension;
    String description;
	
    /**
     * constructor to create an object to filter out all non applicable files
     *
     * @param exten a value of type 'String[]'
     * @param descript a value of type 'String'
     */
    public ExtensionFileFilter( String[] exten, String descript ) {
	extension = new String[ exten.length ];
	
	for( int i = 0 ; i < exten.length ; i++ ) {
	    extension[ i ] = exten[ i ].toLowerCase();
	} // for
	description = descript;
    } // contructor
    
    /**
     * This will tell the JFileChooser if the files are 
     * acceptable under the terms of this extension filter
     *
     * @param	file	the file is question
     *
     * @return 	a yes or no weather the parameter is acceptable
     */
    public boolean accept( File file ) {

	if( file.isDirectory() ) {
	    return true;
	} // if
	
	String fileName = file.getName().toLowerCase();

	for( int i = extension.length - 1 ; i >= 0 ; i -= 1 ) {

	    if( fileName.endsWith( extension[ i ] ) ) {
		return true;
	    } // if
	} // for

	return false;
    } // accept
    
    /**
     * This will return the 'description' of the Filefilter
     *
     * @return 	the description of the file filter
     */
    public String getDescription() {
	return description;
    } // getDescription

} // ExtensionFileFilter

