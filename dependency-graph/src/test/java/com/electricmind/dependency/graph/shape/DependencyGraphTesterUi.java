package com.electricmind.dependency.graph.shape;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class DependencyGraphTesterUi extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> launch());
	}

	private static void launch() {
		DependencyGraphTesterUi ui = new DependencyGraphTesterUi();
		ui.setVisible(true);
	}

	public DependencyGraphTesterUi() {
		super("Dependency Graph Tester UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DependencyGraphTester<?>[] testers = createTesters();
		JList<DependencyGraphTester<?>> testersList = new JList<>(testers);
		add(testersList, BorderLayout.WEST);

		GrapherUi<String> grapherUi = new GrapherUi<String>();

		add(new JScrollPane(grapherUi), BorderLayout.CENTER);

		ListSelectionModel selectionModel = testersList.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selIdx = selectionModel.getMinSelectionIndex();
				@SuppressWarnings("unchecked")
				DependencyGraphTester<String> item = selIdx >= 0 ?
						(DependencyGraphTester<String>) testersList.getModel().getElementAt(selIdx) : null;
				grapherUi.setTester(item);
			}
		});

		pack();
	}

	public static final class GrapherUi<T> extends JPanel {
		private static final long serialVersionUID = 1L;

		private DependencyGraphTester<T> tester;
		private DependencyManager<T> manager;
		private Grapher<T> grapher;

		public GrapherUi() {
			setPreferredSize(new Dimension(800, 600));
			setOpaque(true);
		}

		public void setTester(DependencyGraphTester<T> value) {
			tester = value;
			manager = tester == null ? null : tester.createManager();
			grapher = tester == null ? null : tester.createGrapher(manager);
			grapher.initialize();
			setSize(grapher.getShape().getDimension());

			repaint();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			if (grapher == null) {
				super.paintComponent(g);
				return;
			}
			super.paintComponent(g);

			grapher.draw((Graphics2D)g, getBounds());
		}
	}

	private DependencyGraphTester<?>[] createTesters()
	{
		List<DependencyGraphTester<?>> testers = new ArrayList<>();
		testers.add(new SimpleArtifactGraph());
		testers.add(new SimplePackageGraph());
		testers.add(new PackageGraphWithCrossings());
		testers.add(new PackageGraphWithSimpleCycles());
		testers.add(new PackageGraphWithMultiplePrefixes());
		testers.add(new PackageGraphWithAmbiguousCrossings());
		testers.add(new SimpleNodeDiagram());
		testers.add(new RandomNodesDiagram(20, 100));
		testers.add(new RandomNodesDiagram(30, 150));
		testers.add(new RandomNodesDiagram(50, 400));
		testers.add(new ComplicatedPackageGraphCrossings());
		return testers.toArray(new DependencyGraphTester<?>[testers.size()]);
	}
}
