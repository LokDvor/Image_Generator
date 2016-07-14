import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window extends JFrame implements Runnable
{
	private JPanel render_panel = new JPanel();
	private BufferedImage image;
	private Color color;
	private Graphics g;
	private int size_x = 600, size_y = 600 ;
	
	private int speed = 4;
	
	private int bend_width = 100;
	private int bend_height = 200;
	private int distance;
	private int y_cord = size_y;
	private int num = 10;
	private int tick;
	private int image_num = 0;
	private boolean isRunning;
	
	public Window(String[] args)
	{
		args_parser(args);
		init();
	}
	
	public void args_parser(String[] args)
	{
		size_x = Integer.parseInt(args[0]);
		size_y = Integer.parseInt(args[1]);
		
		if (size_x > 0 && size_y > 0  && size_x < 3000 && size_y < 3000)
		{
			System.out.println("Frame width = " + args[0]);
		
			System.out.println("Frame height = " + args[1]);
		}
		else 
		{
			System.out.println("Frame values out of range (0 - 3000)");
			System.exit(-1);
		}
		
		bend_width = Integer.parseInt(args[2]);
		bend_height = Integer.parseInt(args[3]);
		
		if (bend_width> 0 && bend_height > 0  && bend_width < size_x && bend_height < size_y)
		{
			System.out.println("Bend width = " + bend_width);
		
			System.out.println("Frame height = " + bend_height);
		}
		else 
		{
			System.out.println("Bend values out of range (0 - Frame size)");
			
			System.exit(-1);
		}
		
		distance = Integer.parseInt(args[4]);
		
		if (distance > size_y && distance < 5000)
		{
			System.out.println("Distance = " + distance);
		}
		else 
		{
			System.out.println("Distance values out of range (Frame_y - 5000)");
			
			System.exit(-1);
		}
		
		speed = Integer.parseInt(args[5]);
		
		if (speed < 50 && speed > -50)
		{
			System.out.println("Speed = " + speed);
		}
		else 
		{
			System.out.println("Speed values out of range (- 50 - 50)");
			
			System.exit(-1);
		}
		
		num = Integer.parseInt(args[6]);
		
		if (num > 0 && num < 50)
		{
			System.out.println("Number of frames = " + num);
		}
		else 
		{
			System.out.println("Num of Frames values out of range (0 - 50)");
			
			System.exit(-1);
		}
	}
	
	
	public void init()
	{
		this.setSize(size_x, size_y);
		this.setResizable(false);
		this.add(render_panel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		g = render_panel.getGraphics();
		
		render(g);
		isRunning = true;
		new Thread(this).start();
	}
	
	
	
	public void render(Graphics g)
	{
		image = new BufferedImage(size_x, size_y,  BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = image.createGraphics();
		
		g2.setColor(Color.BLUE);				
		g2.fillRect(0, 0, size_x, size_y);
		
		g2.setColor(Color.RED);	
		g2.fillRect( (int)(size_x/2 - bend_width/2) , y_cord, bend_width, bend_height);
			
		
		g.drawImage(image, 0, 0, null);
		
		
		
	}
	
	
	
	public static void main(String[] args)
	{
		Window window = new Window(args);
		
	}


	@Override
	public void run() 
	{
		while (isRunning)
		{
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			
			render(g);
			
			if (y_cord < -1*bend_height)
			{
				y_cord = distance;
			}
			
			if (y_cord >  distance)
			{
				y_cord =  - 1;
			}
			
			y_cord = y_cord + speed;
			tick++;
			
			
			if ((tick == 50) && (num != 0))
			{
			try 
			{  
				File outputFile = new File("./Stream/IMAGE_N" + image_num + ".png");
				outputFile.getParentFile().mkdirs();
				ImageIO.write(image, "png", outputFile);
				image_num++;
				num--;
				tick = 0;
			}
		
				
			catch (IOException e) 	
			{
			e.printStackTrace();
			}
			
			if (num == 0)
			{
	
				isRunning = false;
				g.dispose();
				System.out.println("Finished: Success!!");
				System.exit(0);
			}
		}
		
	}
	}
}
