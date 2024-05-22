package es.upm.pproject.parkingjam.parking_jam.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import es.upm.pproject.parkingjam.parking_jam.controller.controller;
import es.upm.pproject.parkingjam.parking_jam.model.Level;
import es.upm.pproject.parkingjam.parking_jam.model.Vehicle;
import javafx.scene.layout.Border;
//import javafx.event.ActionEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Box;
//import javafx.scene.text.Font;
import javafx.util.Pair;



public class view extends JFrame {
    private Map<Character, Vehicle> mapVehiculo; // Mapa con las posiciones de los vehículos
    private Map <Character,Pair<Integer,Integer>> mapPosiciones;
	private Map <Character,Pair<Integer,Integer>> mapCoordenadas;
    private Level level;
	private char carSelect;
    private controller controller;
	private Graphics grafic;
	private Component comp;
    private int dimensionMapaX;
	private int dimensionMapaY;
	private int tamanoCeldaX;
	private int tamanoCeldaY;
    private Image cocheRojoHorizontalImage;
    private Image cocheRojoVerticalImage;// Imagen del coche
    private Image parkingImage;
    private Image cocheAzulVerticalImage;
    private Image cocheAzulHorizontalImage;
    private Image camionVerticalImage;
    private Image camionHorizontalImage;
    private Image salida_arribaImage;
    private Image salida_abajoImage;
    private Image salida_derechaImage;
    private Image salida_izquierdaImage;

    public view( Map<Character,Pair<Integer,Integer>> posiciones, Level level,controller controller) {
        this.mapVehiculo = level.getCars();
        this.mapPosiciones=posiciones;
        this.dimensionMapaX=level.getDimensionX();
		this.dimensionMapaY=level.getDimensionY();
		tamanoCeldaX=Math.round((400+(dimensionMapaX/2))/(dimensionMapaX-2));
		tamanoCeldaY=Math.round((400+(dimensionMapaY/2))/(dimensionMapaY-2)) ;
		System.out.println("tamaño celda " +tamanoCeldaX);
        this.level=level;
        this.controller=controller;
		mapCoordenadas=cambioCoodenadas(posiciones);
        initUI();
    }

	private Map <Character,Pair<Integer,Integer>> cambioCoodenadas (Map <Character,Pair<Integer,Integer>> mapPosiciones){
		Map <Character,Pair<Integer,Integer>> sol = new HashMap<Character,Pair<Integer,Integer>>();
		Iterator <Character> it = mapPosiciones.keySet().iterator();
		int x;
		int y;
		int coordX=0;
		int coordY=0;
		while (it.hasNext()) 
		{
			char car = it.next();
			x=mapPosiciones.get(car).getKey();
			y=mapPosiciones.get(car).getValue();
			if(x==0) coordX=100;
			if(y==0) coordY=0;
			if(x==1) coordX=150;
			if(y==1) coordY=50;
			if(x>1) coordX= 150 + (tamanoCeldaX*(x-1));
			if(y>1) coordY= 50 + (tamanoCeldaY*(y-1));
			sol.put(car,new Pair<>(coordX,coordY));
		}
		return sol;
	}

    private void initUI() {
        setTitle("Parking Game");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);  // Impide que la ventana sea redimensionable

        setLayout(new BorderLayout());

        // Cargar la imagen del coche
        ImageIcon cocheRojoHorizontal = new ImageIcon(getClass().getResource("/images/coche_rojo_horizontal.png"));
        ImageIcon cocheRojoVertical = new ImageIcon(getClass().getResource("/images/coche_rojo_vertical.png")); // Asegúrate de que el path es correcto
        ImageIcon parkingIcon = new ImageIcon(getClass().getResource("/images/parking2.jpg"));
        ImageIcon cocheAzulHorizontal = new ImageIcon(getClass().getResource("/images/coche_azul_horizontal.png"));
        ImageIcon cocheAzulVertical = new ImageIcon(getClass().getResource("/images/coche_azul_vertical.png"));
        ImageIcon camionHorizontal = new ImageIcon(getClass().getResource("/images/camion_horizontal.png"));
        ImageIcon camionVertictal = new ImageIcon(getClass().getResource("/images/camion_vertical.png"));
        ImageIcon salidaArriba = new ImageIcon(getClass().getResource("/images/salida_arriba.png"));
        ImageIcon salidaAbajo = new ImageIcon(getClass().getResource("/images/salida_abajo.png"));
        ImageIcon salidaDerecha = new ImageIcon(getClass().getResource("/images/salida_derecha.png"));
        ImageIcon salidaIzquierda = new ImageIcon(getClass().getResource("/images/salida_izquierda.png"));
        ImageIcon arbol = new ImageIcon(getClass().getResource("/images/arbol.png"));
		ImageIcon planta = new ImageIcon(getClass().getResource("/images/planta.png"));
        cocheRojoHorizontalImage = cocheRojoHorizontal.getImage().getScaledInstance(133, 67, Image.SCALE_SMOOTH); 
        cocheRojoVerticalImage = cocheRojoVertical.getImage().getScaledInstance(67, 133, Image.SCALE_SMOOTH);  // cada cuadrado es 67 px
        parkingImage= parkingIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        cocheAzulVerticalImage = cocheAzulVertical.getImage().getScaledInstance(67, 133, Image.SCALE_SMOOTH); 
        cocheAzulHorizontalImage = cocheAzulHorizontal.getImage().getScaledInstance(133, 67, Image.SCALE_SMOOTH); 
        camionVerticalImage = camionVertictal.getImage().getScaledInstance(67, 200, Image.SCALE_SMOOTH); 
        camionHorizontalImage = camionHorizontal.getImage().getScaledInstance(200, 67, Image.SCALE_SMOOTH); 
        salida_arribaImage=salidaArriba.getImage().getScaledInstance(67     , 50, Image.SCALE_SMOOTH);
        salida_abajoImage=salidaAbajo.getImage().getScaledInstance(67     , 50, Image.SCALE_SMOOTH); 
        salida_derechaImage=salidaDerecha.getImage().getScaledInstance(50     , 67, Image.SCALE_SMOOTH); 
        salida_izquierdaImage=salidaIzquierda.getImage().getScaledInstance(50     , 67, Image.SCALE_SMOOTH);
        Image arbolImg1 = arbol.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		Image arbolImg2 = arbol.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		Image plantaImg1 = planta.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
         
		Dimension buttonSize = new Dimension(40,40);
		Dimension buttonSize2 = new Dimension(195,40);
		//Color buttonColor = new Color(0,122,63);
		//Color buttonColor = new Color(50,150,90); //verde oscuro
		//Color buttonColor = new Color(26,127,217); //azul
		//Color buttonColor = new Color(129,48,217); //morado
		//Color buttonColor = new Color(123,189,1);  //mismo verde
		Color buttonColor = new Color(65,130,4); // mismo verde oscuro
		Color winPColor = new Color(153,191,82);
		//Color winPColor = new Color(157,191,98);
		//Color winPColor = new Color(120,161,82);
		Color borderWinPColor = new Color(50,150,90);
		Color shadeWinPColor = new Color(57,64,50);

		//fuentes de texto
		Font titleFont = null;
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/titlefont.ttf")).deriveFont(35f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font gamePointsFont = null;
		try {
			gamePointsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/pointsfont.ttf")).deriveFont(23f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font levelPointsFont = null;
		try {
			levelPointsFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/pointsfont.ttf")).deriveFont(27f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		Font menuFont = null;
		try {
			menuFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/menuText.ttf")).deriveFont(16f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}

		//iconos
		ImageIcon menuIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/menu.png")),30,30);
		ImageIcon restartIcon = resizeIcon( new ImageIcon(getClass().getResource("/icons/restart.png")),30,30);
		ImageIcon undoIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/undo.png")),30,30);
		ImageIcon starIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/yellowstar.png")),30,30);
		ImageIcon starIcon2 = rotateIcon(starIcon, 180);
		ImageIcon closeMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/close.png")),30,30);
		ImageIcon addMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/add.png")),30,30);
		ImageIcon saveMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/save.png")),30,30);
		ImageIcon loadMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/upload.png")),30,30);
		ImageIcon levelsMIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/levelsMenu.png")),30,30);


        // Panel donde se dibujan los vehículos
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
				grafic=g;
				comp=this;
                g.drawImage(parkingImage,100,0 , this);
                Iterator <Character> it = mapCoordenadas.keySet().iterator();
                while (it.hasNext()) {
                    Character caracter = it.next();
                    boolean esRojo=false;
                    boolean esSalida=false;
                    if(caracter.equals('*')) esRojo=true;
                    if(caracter.equals('@')) esSalida=true;
                    Pair <Boolean,Integer> dimension = conseguirPosicion(caracter);
                    Pair <Integer,Integer> posicion= mapCoordenadas.get(caracter);
                    pintarVehiculo(esRojo,esSalida,dimension.getValue(),dimension.getKey(),posicion.getKey(),posicion.getValue(),g,this);
                }
                

                //arboles de fondo
                g.drawImage(arbolImg1, 20, 30, this);
                g.drawImage(arbolImg1, 600, 90, this);
                g.drawImage(arbolImg1, 630, 240, this);
                g.drawImage(arbolImg2, 40, 430, this);
                g.drawImage(arbolImg2, 640, 20, this);
                g.drawImage(plantaImg1, 600, 370, this);
                g.drawImage(plantaImg1, 15, 280, this);
                g.drawImage(plantaImg1, 630, 450, this);
                }
            
        };
        gamePanel.setBackground(new Color(123,189,1));
        
//--------------------PANEL NORTH-----------------------------
		// Header del view
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

		//estructura del header
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10)); 

		JPopupMenu menuPanel = new JPopupMenu();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuPanel.setPopupSize(207,235);
		menuPanel.setBounds(50, 87, 207, 235);
		
		JButton levelsB= new JButton("levels menu");
		levelsB.setPreferredSize(buttonSize2);
		levelsB.setIcon(levelsMIcon);
		levelsB.setBackground(buttonColor);
		levelsB.setForeground(Color.white);
		levelsB.setFont(menuFont);
		levelsB.setHorizontalAlignment(SwingConstants.LEFT);
		levelsB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("levels button pressed");
			}
		});
		JButton saveB = new JButton("save game");
		saveB.setPreferredSize(buttonSize2);
		saveB.setIcon(saveMIcon);
		saveB.setBackground(buttonColor);
		saveB.setForeground(Color.white);
		saveB.setFont(menuFont);
		saveB.setHorizontalAlignment(SwingConstants.LEFT);
		saveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("save button pressed");
			}
		});
		JButton loadB= new JButton("load game");
		loadB.setPreferredSize(buttonSize2);
		loadB.setIcon(loadMIcon);
		loadB.setBackground(buttonColor);
		loadB.setForeground(Color.white);
		loadB.setFont(menuFont);
		loadB.setHorizontalAlignment(SwingConstants.LEFT);
		loadB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("load button pressed");
			}
		});
		JButton newGameB = new JButton("new game");
		newGameB.setPreferredSize(buttonSize2);
		newGameB.setIcon(addMIcon);
		newGameB.setBackground(buttonColor);
		newGameB.setForeground(Color.white);
		newGameB.setFont(menuFont);
		newGameB.setHorizontalAlignment(SwingConstants.LEFT);
		newGameB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("nwe game button pressed");
			}
		});
		JButton closeB = new JButton("close Parking Jam");
		closeB.setPreferredSize(buttonSize2);
		closeB.setIcon(closeMIcon);
		closeB.setBackground(buttonColor);
		closeB.setForeground(Color.white);
		closeB.setFont(menuFont);
		closeB.setHorizontalAlignment(SwingConstants.LEFT);
		closeB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("close button pressed");
				dispose();
			}
		});

		menuPanel.add(levelsB);
		menuPanel.add(saveB);
		menuPanel.add(loadB);
		menuPanel.add(newGameB);
		menuPanel.add(closeB);
		
		JButton menuB = new JButton(menuIcon);
		menuB.setPreferredSize(buttonSize);
		menuB.setBackground(buttonColor);
		menuB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("menu button pressed ");
		
				if(!menuPanel.isVisible()) {
					menuPanel.show(view.this, 50,87);
					menuPanel.setVisible(true);
				} else {
					menuPanel.setVisible(false);
				}
			}
		});
		
		JButton restartB = new JButton(restartIcon);
		restartB.setPreferredSize(buttonSize);
		restartB.setBackground(buttonColor);
		restartB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* TODO: 
				 * notificar al controller 'restart' pulsado
				 * recibir nueva situación del tablero y los puntos 
				 * pintar 
				 */
				System.out.println("restart button pressed");
			}

		});

		JButton undoB = new JButton(undoIcon);
		undoB.setPreferredSize(buttonSize);
		undoB.setBackground(buttonColor);
		undoB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* TODO: 
				 * notificar al controller 'undo' pulsado
				 * recibir nueva situación del tablero y los puntos 
				 * pintar 
				 */
				System.out.println("undo button pressed");
			}

		});

		JLabel gamePointsLabel = new JLabel("Game Points: "); 
		if(gamePointsFont!=null){ gamePointsLabel.setFont(gamePointsFont); }
		else {gamePointsLabel.setFont(new Font("Serif",Font.PLAIN,25)); }

		JLabel gamePointsValue= new JLabel(level.getGamePoints().toString());
		if(gamePointsFont!=null){ gamePointsValue.setFont(gamePointsFont); }
		else {gamePointsValue.setFont(new Font("Serif",Font.PLAIN,25)); }

		row1.add(Box.createHorizontalStrut(30));
		row1.add(menuB);
		row1.add(Box.createHorizontalStrut(15));
		row1.add(restartB);
		row1.add(Box.createHorizontalStrut(15));
		row1.add(undoB);
		row1.add(Box.createHorizontalStrut(195));
		row1.add(gamePointsLabel);
		row1.add(gamePointsValue);
		row1.add(Box.createHorizontalStrut(30));
		headerPanel.add(row1);

		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		JLabel title = new JLabel(level.getTitle()); //debe recibir el title del nivel del controller
		if(titleFont!=null){ title.setFont(titleFont); }
		else {title.setFont(new Font("Serif",Font.PLAIN,40)); }
		row2.add(title);
		headerPanel.add(row2);

		JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
		JLabel star1 = new JLabel(starIcon);

		JLabel levelPointsValue = new JLabel(level.getLevelPoint().toString());
		if(levelPointsFont!=null){ levelPointsValue.setFont(levelPointsFont); }
		else {levelPointsValue.setFont(new Font("Serif",Font.PLAIN,30));}

		JLabel star2 = new JLabel(starIcon2);

		row3.add(star1);
		row3.add(levelPointsValue);
		row3.add(star2);
		headerPanel.add(row3);

		add(headerPanel, BorderLayout.NORTH); 

//-------------------------PANEL CENTRAL--------------------

		JLayeredPane layeredP = new JLayeredPane();
		layeredP.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
		
		JPanel winPanel = new JPanel();
		winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.Y_AXIS));
		winPanel.setPreferredSize(new Dimension(200,200));
		winPanel.setBounds(75, 150, 100, 100);
		winPanel.setBackground(winPColor);
		BevelBorder b = new BevelBorder(BevelBorder.RAISED, borderWinPColor, shadeWinPColor); 
		winPanel.setBorder(b);
		
		JLabel winL = new JLabel("VICTORY");
		winL.setFont(titleFont);
		
		JLabel pointsWL = new JLabel("0000");
		pointsWL.setFont(levelPointsFont);
		
		JLabel star1W = new JLabel(starIcon);
		JLabel star2W = new JLabel(starIcon2);
		
		JButton levelsWB = new JButton(levelsMIcon);
		levelsWB.setBackground(buttonColor);
		levelsWB.setPreferredSize(buttonSize);
		
		JButton restartWB = new JButton(restartIcon);
		restartWB.setBackground(buttonColor);
		restartWB.setPreferredSize(buttonSize);
		
		JButton nextWB = new JButton("n");
		nextWB.setBackground(buttonColor);
		nextWB.setPreferredSize(buttonSize);
		
		JPanel row1W = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel row2W = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel row3W = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		row1W.add(winL);
		row2W.add(star1W);
		row2W.add(levelsWB);
		row2W.add(star2W);
		row3W.add(Box.createHorizontalStrut(25));
		row3W.add(levelsWB);
		row3W.add(Box.createHorizontalStrut(15));
		row3W.add(restartWB);
		row3W.add(Box.createHorizontalStrut(15));
		row3W.add(nextWB);
		row3W.add(Box.createHorizontalStrut(25));
		
		//layeredP.add(winPanel, JLayeredPane.PALETTE_LAYER);

        add(gamePanel, BorderLayout.CENTER);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
				if(e.getX()>150 && e.getX()<550 && e.getY()<450 && e.getY()>50)
				{
				int clickX = e.getX();
				int clickY=e.getY();
				
                int x = ((clickX-150)/tamanoCeldaX) ; //400 pixeles de ancho y largo el gris del parking
                int y = ((clickY-50)/tamanoCeldaY);
               
                System.out.println("Pressed at (" + x + ", " + y + ")");
				}
				carSelect = controller.click(new Pair<Integer,Integer>(e.getX(),e.getY()));
				System.out.println("id pinchado: "+carSelect);
				System.out.println("coordenadas pinchadas: "+e.getX()+" , " +e.getY());
            }
			@Override
            public void mouseReleased(MouseEvent e) {
                //drop
				if(carSelect!=' ')
				{
				Pair<Integer,Integer> newPos = new Pair<Integer,Integer>(null, null); // pair < par<int,int>,int>
				Pair<Pair<Integer,Integer>,Integer> newInfo = new Pair<Pair<Integer,Integer>,Integer>(null, null); 
				System.out.println("Mouse Soltado at: " + e.getX() + ", " + e.getY());
              	newInfo = controller.drop(new Pair<Integer,Integer>(e.getX(), e.getY()));
              	newPos = newInfo.getKey();
				if(newPos!=null)
				{
				mapPosiciones.put(carSelect, newPos);
				mapCoordenadas= cambioCoodenadas(mapPosiciones);
				levelPointsValue.setText(newInfo.getValue().toString());
                gamePanel.repaint();
                
				}
			}
            }
			
    });
        
 
        
   


	gamePanel.addMouseMotionListener(new MouseAdapter() {
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse Dragged to: " + e.getX() + ", " + e.getY());
	if(carSelect!=' ')
	{
		Pair <Integer,Integer>desp=new Pair<Integer,Integer>(null, null);
		desp = controller.hold(new Pair<Integer,Integer>(e.getX(), e.getY()));
		
		if(desp.getKey()!=0)
		{
			int x= mapCoordenadas.get(carSelect).getKey();
			int y= mapCoordenadas.get(carSelect).getValue();
			x= x+ desp.getKey();
			mapCoordenadas.put(carSelect, new Pair<>(x, y));
		}
		else if(desp.getValue()!=0)
		{
			int x= mapCoordenadas.get(carSelect).getKey();
			int y= mapCoordenadas.get(carSelect).getValue();
			y= y+ desp.getValue();
			mapCoordenadas.put(carSelect, new Pair<>(x, y));
		}
		gamePanel.repaint();
}
}
});


	
     
    }
    public Pair<Boolean,Integer> conseguirPosicion(Character car){
        Pair<Boolean,Integer> sol=null;
        if(car.equals('@'))
        {
            Pair<Boolean,Integer> par= new Pair<Boolean,Integer>(false, 1);
            sol=par;
        }
        else{

            int x = mapVehiculo.get(car).getDimension().getKey();
            int y=mapVehiculo.get(car).getDimension().getValue();

            if(x==1) 
            {
                Pair<Boolean,Integer> par= new Pair<Boolean,Integer>(true, y);
                sol=par;
            }
            else if(y==1) 
            {
                Pair<Boolean,Integer> par= new Pair<Boolean,Integer>(false, x);
                sol=par;
            }
        }
        return sol;
    }
    public void pintarVehiculo(boolean esRojo,boolean esSalida, int tamano, boolean esVertical, int   posX, int posY, Graphics g, Component context) {
        Image vehiculoPintar = null; // Asegúrate de que esté inicializada a null
        int x=posX;
        int y=posY;
            if(esSalida){
            if(posX==100)
            {
                vehiculoPintar=salida_izquierdaImage;
             
            }
            if(posX>=550)
            {
             vehiculoPintar=salida_derechaImage;
            x=550;
            }
            if(posY==0)
            {
                vehiculoPintar=salida_arribaImage;
                
            }
            if(posY>=450)
            {
                vehiculoPintar=salida_abajoImage;
            y=450;
            }
        }
        else if (esRojo && !esVertical) {
            vehiculoPintar = cocheRojoHorizontalImage;
        } else if( esRojo && esVertical){
            vehiculoPintar=cocheRojoVerticalImage;
        } else if (tamano == 2 && esVertical) {
            vehiculoPintar = cocheAzulVerticalImage;
        } else if (tamano == 2 && !esVertical) {
            vehiculoPintar = cocheAzulHorizontalImage;
        } else if (tamano == 3 && esVertical) {
            vehiculoPintar = camionVerticalImage;
        } else if (tamano == 3 && !esVertical) {
            vehiculoPintar = camionHorizontalImage;
        }
    
        if (vehiculoPintar != null) {
            g.drawImage(vehiculoPintar, x, y, context);
        }

        
    }

	private void moverImagen()
	{

	}
	private ImageIcon resizeIcon(ImageIcon icon, int i, int j) {
		Image img = icon.getImage();
		Image resizedImg = img.getScaledInstance(i, j, Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImg);
	}
	
	private ImageIcon rotateIcon(ImageIcon icon, double angle) {
		Image img = icon.getImage();
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        // Rotar la imagen usando AffineTransform
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), img.getWidth(null) / 2, img.getHeight(null) / 2);
        g2d.setTransform(transform);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(bufferedImage);
	}


    public static void main(String[] args) throws FileNotFoundException, IOException {
         Map<Character,Pair<Integer,Integer>> mapPosiciones= new HashMap<>();
         Level level = new Level(1);
         controller cont= new controller();

        mapPosiciones.put('@', new Pair<>(4,7 ));
        mapPosiciones.put('*', new Pair<>(4, 2));
        mapPosiciones.put('a', new Pair<>(1, 1));
        mapPosiciones.put('b', new Pair<>(3, 1));
        mapPosiciones.put('c', new Pair<>(6, 1));
        mapPosiciones.put('d', new Pair<>(1, 3));
        mapPosiciones.put('e', new Pair<>(2,5));
        mapPosiciones.put('f', new Pair<>(3, 4));
        mapPosiciones.put('g', new Pair<>(4, 6));
        
        SwingUtilities.invokeLater(() -> {
            view game = new view(mapPosiciones,level,cont);
            game.setVisible(true);
        });
    }
}
