package org.example;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Collections;

public class Tree {
    private List<Point> puncte;
    private List<Line> linii_existante;
    public Tree(List<Point> puncte, List<Line> linii_existante)
    {
        this.linii_existante=new ArrayList<>(linii_existante);
        this.puncte=new ArrayList<>(puncte);
    }
    public List<List<Line>> generare_all_tree()
    {
        List<Line> toate_liniile=generate_toate_liniile();
        List<List<Line>> toti_arborii = new ArrayList<>();
        generare_tree(toate_liniile,new ArrayList<>(), toti_arborii,puncte.size());
        toti_arborii.sort(Comparator.comparingDouble(this::calcul_tree_cost));
        return toti_arborii;
    }
    private List<Line> generate_toate_liniile()
    {
        List<Line> linii=new ArrayList<>();
        for(int i=0;i<puncte.size();i++)
            for(int j=i+1;j<puncte.size();j++)
            {
                Line linie=new Line(puncte.get(i),puncte.get(j), Color.BLACK);
                if(!linii_existante.contains(linie))
                {
                    linii.add(linie);
                }
            }
        return linii;
    }
    private void generare_tree(List<Line> linii_disponibile,List<Line> arbore_curent,List<List<Line>> toti_arborii,int nr_puncte)
    {
        if(arbore_curent.size()==nr_puncte-1)
        {
            if(este_arbore(arbore_curent,nr_puncte))
            {
                toti_arborii.add(new ArrayList<>(arbore_curent));
            }
            return;
        }
        for(Line linie:linii_disponibile)
        {
            if(!arbore_curent.contains(linie))
            {
                arbore_curent.add(linie);
                generare_tree(linii_disponibile,arbore_curent,toti_arborii,nr_puncte);
                arbore_curent.remove(linie);
            }
        }
    }
    private boolean este_arbore(List<Line> tree,int nr_puncte)
    {
        DisjointSet ds=new DisjointSet(nr_puncte);
        for(Line line:tree)
        {
            int u=puncte.indexOf(line.getStart());
            int v=puncte.indexOf(line.getEnd());
            ds.union(u,v);
        }
        return ds.isConnected();
    }
    private double calcul_tree_cost(List<Line> tree)
    {
        return tree.stream().mapToDouble(Line::getLength).sum();
    }
}


























