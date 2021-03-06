import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.*;
import org.jgrapht.generate.*;
import org.jgrapht.*;

class MapComponent extends JComponent {
    private WorldMap wm;
    private List<Point2D> path;
    //private BufferedImage image;

    public MapComponent(WorldMap wm) {
	this.wm = wm;
         //image = new BufferedImage((int) wm.getBorder().getWidth(), (int) wm.getBorder().getHeight(), BufferedImage.TYPE_INT_ARGB);
	setPreferredSize(new Dimension((int) wm.getBorder().getWidth(),(int) wm.getBorder().getHeight()));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g;
	drawWalls(g2d);
	drawTiles(g2d);
         drawPath(g2d);
    }

    private void drawWalls(Graphics2D g) {
        Iterator<Shape> i = wm.wallIterator();
        while (i.hasNext()) {
            Shape s = i.next();
            g.fill(s);
        }
    }

    private void drawTiles(Graphics2D g) {
	TileWorldMap twm = new TileWorldMap(wm, Main.TILE_SIZE);
	Iterator<TileWorldMap.Tile> i = twm.tileIterator();
	while (i.hasNext()) {
	    g.draw(i.next().getRectangle());
	}
    }

    private void drawPath(Graphics2D g) {
        g.setColor(Color.RED);
        Iterator<Point2D> i = path.iterator();
        Point2D last = i.next();
        while (i.hasNext()) {
            Point2D n = i.next();
            g.drawLine((int) last.getX(), (int) last.getY(), (int) n.getX(), (int) n.getY());
            last = n;
        }
    }

    public void setPath(List<Point2D> path) {
        this.path = path;
    }
}

public class Main {
    public static final double TILE_SIZE = 10;
    public static void main(String[] argv) {
	List<Shape> walls = new ArrayList<Shape>();
	walls.add(new Rectangle2D.Double(295, 0, 10, 305));
	walls.add(new Rectangle2D.Double(0, 295, 195, 10));
	walls.add(new Rectangle2D.Double(245, 295, 50, 10));

	walls.add(new Rectangle2D.Double(350, 150, 100, 100));

	WorldMap wm = new WorldMap(walls, 0,0,600,400);
	TilingGraphBuilder tgb = new TilingGraphBuilder(wm, Main.TILE_SIZE);
	
	DefaultDirectedWeightedGraph<TileWorldMap.Tile, DefaultWeightedEdge> tileGraph = tgb.buildGraph();

	try {
	    DOTExporter de = new DOTExporter();
	    FileWriter out = new FileWriter("tilegraph.dot");
	    de.export(out, tileGraph);
	    out.close();
	}
	catch (IOException e) {
	    throw new RuntimeException();
	}

         DijkstraPathfinder<TileWorldMap.Tile, DefaultWeightedEdge>.Path path = new DijkstraPathfinder<TileWorldMap.Tile, DefaultWeightedEdge>().findPath(tileGraph, tgb.getTilingWorldMap().new Tile(0), tgb.getTilingWorldMap().new Tile(59));
         List<TileWorldMap.Tile> vertices = path.getVertices();
         System.out.println(vertices.toString());

         ArrayList<Point2D> pointPath = new ArrayList<Point2D>();
         for (TileWorldMap.Tile t : vertices) {
             pointPath.add(t.getCenter());
         }


         MapComponent mc = new MapComponent(wm);
         mc.setPath(pointPath);

	JFrame frame = new JFrame("Demo");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(mc);
	frame.pack();
	frame.setVisible(true);
	
	//random_graph();
    }



//    public static void random_graph() {
//	RandomGraphGenerator<Integer, DefaultEdge> rgg = new RandomGraphGenerator<Integer, DefaultEdge>(100, 300);
//	DefaultDirectedGraph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
//
//	VertexFactory<Integer> fac = new VertexFactory<Integer>() {
//	    private int n = 0;
//
//	    public Integer createVertex() {
//		return new Integer(n++);
//	    }
//	};
//
//	rgg.generateGraph(graph, fac, null);
//
//	try {
//	    DOTExporter de = new DOTExporter();
//	    FileWriter out = new FileWriter("randomgraph.dot");
//	    de.export(out, graph);
//	    out.close();
//	}
//	catch (IOException e) {
//	    throw new RuntimeException();
//	}
//    }
}