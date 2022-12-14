
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

import java.io.File;

public class WebCam implements Runnable 
{
	final int INTERVAL = 1000;
	CanvasFrame canvas  = new CanvasFrame("Web Cam");
	public boolean runWebCam = true;
	public static String file;
	
	public WebCam()
	{
	  canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}
	
	public boolean getRunWebCam()
	{
	  return runWebCam;
	}
	
	public void setRunWebCam(boolean b)
	{
	  runWebCam = b;
	}
	
	@Override
	public void run() 
	{
		FrameGrabber grabber = new VideoInputFrameGrabber(0);
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		IplImage img;
		int i = 0;
		
		  try
		  {
		    grabber.start();
		  }
		  catch(Exception e)
		  {
		    return;
		  }
		  while(runWebCam)
		  {
	        try
	        {
    		    Frame frame = grabber.grab();
    		    img = converter.convert(frame);
    		    
    		    cvFlip(img, img, 1);
    		    if(i > 10)
    		    {
    		      i = 0;
    		    }
    		    cvSaveImage(file + "\\img-" + (i++) + ".jpg", img);
    		    canvas.showImage(converter.convert(img));
	        }
	        catch(Exception e)
	        {
	          //usually throws an exception the first time just continue
	          e.printStackTrace();
	        }
  		  try
		    {
  		    Thread.sleep(INTERVAL);
		    }
    		catch(Exception e)
    		{
    		}
		  }
	}
	
	public static void main(String[] args){
	if(args.length == 2) {
		if(args[0].compareToIgnoreCase("-f") == 0) {
			file = args[1];
			File f = new File(file);
			if(!f.isDirectory()){
				System.out.println(f + " is not a file! Exiting");
			}
			else {
				System.out.println(f + " found");
			}
		}
		else {
			System.out.println("Pass in parameters -f DIRECTORY_PATH");
			return;
		}
	}
    WebCam webcam = new WebCam();
    Thread thread = new Thread(webcam);
    thread.start();
    
  }
	
}