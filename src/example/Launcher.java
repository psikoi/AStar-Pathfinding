package example;

import example.element.Grid;
import example.element.Tile;
import example.ui.ControlsPanel;
import java.awt.Dimension;
import java.util.ArrayList;
import pathfinding.AStarAlgorithm;
import example.ui.MainFrame;
import example.ui.GridPanel;
import javax.swing.JPanel;

public class Launcher {

    public static final int ROW_COUNT = 20;
    public static final int COLUMN_COUNT = 20;

    private static MainFrame frame;
    private static JPanel container;
    private static GridPanel canvas;
    private static ControlsPanel controls;
    
    private static AStarAlgorithm astar;

    public static void main(String[] args) {
        
        Grid grid = generateGrid(COLUMN_COUNT, ROW_COUNT);
        for (Tile t : grid.getTiles()) {
            t.calculateNeighbours(grid);
        }
        
        astar = new AStarAlgorithm(grid);
        
        initUI();

        astar.addObserver(canvas);
        astar.updateUI();

    }

    private static void initUI() {
        
        int w = COLUMN_COUNT * Tile.TILE_SIZE;
        int h = ROW_COUNT * Tile.TILE_SIZE;
        int controlsW = 200;
        int margin = 10;
        
        frame = new MainFrame();
        frame.setPreferredSize(new Dimension(w + controlsW + 15 + (margin * 3), h + 40 + (margin * 2)));
        
        container = new JPanel();
        container.setLayout(null);
        
        controls = new ControlsPanel(controlsW, 120, astar);
        controls.setBounds(w + (margin * 2), margin, controlsW, h);
        
        canvas = new GridPanel(controls);
        canvas.setBounds(margin, margin, w, h);
        
        
        container.add(controls);
        container.add(canvas);
        
        frame.setContentPane(container);
        frame.setVisible(true);
        frame.pack();
    }

    private static Grid generateGrid(int width, int height) {
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < COLUMN_COUNT; i++) {
            for (int j = 0; j < ROW_COUNT; j++) {
                Tile t = new Tile(i, j);
                tiles.add(t);
            }
        }

        return new Grid(width, height, tiles);

    }

}
