package example.ui;

import example.element.Grid;
import example.element.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import pathfinding.AStarAlgorithm;
import pathfinding.element.Node;

public class GridPanel extends JPanel implements Observer {

    private Grid grid;
    private ArrayList<Tile> path;

    private Tile start;
    private Tile end;

    private ControlsPanel controls;

    private BasicStroke defaultStroke;
    private BasicStroke widerStroke;

    public GridPanel(ControlsPanel controls) {
        this.controls = controls;

        this.defaultStroke = new BasicStroke();
        this.widerStroke = new BasicStroke(2);

        
        setBorder(new LineBorder(Color.gray));
        
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();

                int tileX = x / Tile.TILE_SIZE;
                int tileY = y / Tile.TILE_SIZE;

                Tile t = grid.find(tileX, tileY);

                if (t != null) {
                    controls.selectTile(t);
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);

        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (start != null) {

            int x = (start.getX() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 6;
            int y = (start.getY() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 6;

            g.setColor(new Color(20, 122, 17));
            g.setStroke(widerStroke);
            g.fillOval(x, y, 12, 12);
        }

        if (end != null) {
            int x = (end.getX() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 6;
            int y = (end.getY() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 6;

            g.setColor(new Color(16, 49, 119));
            g.setStroke(widerStroke);
            g.fillOval(x, y, 12, 12);
        }

        if (path != null) {
            g.setColor(new Color(229, 142, 229));
            for (int i = 0; i < path.size() - 1; i++) {
                Tile t = path.get(i);
                Tile t2 = path.get(i + 1);

                int x = (t.getX() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 5;
                int y = (t.getY() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 5;

                int xx = (t2.getX() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2);
                int yy = (t2.getY() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2);

                g.setStroke(widerStroke);
                g.fillOval(x, y, 10, 10);
                g.setStroke(defaultStroke);
                g.drawLine(x + 5, y + 5, xx, yy);
            }
        }

        g.setStroke(defaultStroke);

        if (grid != null && grid.getTiles() != null) {
            for (Tile t : grid.getTiles()) {
                g.setColor(new Color(220, 220, 220));
                g.drawRect(t.getX() * Tile.TILE_SIZE, t.getY() * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
                if (!t.isValid()) {
                    g.setColor(Color.GRAY);
                    int x = (t.getX() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 5;
                    int y = (t.getY() * Tile.TILE_SIZE) + (Tile.TILE_SIZE / 2) - 5;

                    g.fillOval(x, y, 10, 10);
                }
            }
        }

        g.drawRect(getWidth() - 1, 0, 1, getHeight());
        g.drawRect(0, getHeight() - 1, getWidth(), 1);

    }

    @Override
    public void update(Observable o, Object o1) {

        AStarAlgorithm alg = (AStarAlgorithm) o;

        Grid grid = (Grid) alg.getNetwork();
        ArrayList<Node> path = alg.getPath();
        Node start = alg.getStart();
        Node end = alg.getEnd();

        this.grid = grid;

        this.path = new ArrayList<Tile>();

        if (path != null) {
            for (Node n : path) {
                if (n instanceof Tile) {
                    this.path.add((Tile) n);
                }
            }
        }

        if (start != null && start instanceof Tile) {
            this.start = (Tile) start;
        } else {
            this.start = null;
        }

        if (end != null && end instanceof Tile) {
            this.end = (Tile) end;
        } else {
            this.end = null;
        }

        repaint();

    }

}
