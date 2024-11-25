package model;

import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulacaoTimers {
    private List<Corpo> corpos;
    private CorposController corposController;

    public SimulacaoTimers(List<Corpo> corpos) {
        this.corpos = corpos;
        this.corposController = new CorposController();
    }

    public AnimationTimer createGravidadeTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < corpos.size(); i++) {
                    Corpo one = corpos.get(i);
                    for (int j = i + 1; j < corpos.size(); j++) {
                        Corpo two = corpos.get(j);
                        corposController.atualizarVelocidades(one, two);
                    }
                }

                for (Corpo corpo : corpos) {
                    corpo.setTranslateX(corpo.getTranslateX() + corpo.GetVelocidadeX());
                    corpo.setTranslateY(corpo.getTranslateY() + corpo.GetVelocidadeY());
                    corpo.setTranslateZ(corpo.getTranslateZ() + corpo.GetVelocidadeZ());
                }
            }
        };

    }

    public AnimationTimer createColisaoTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                List<Corpo> corpos2 = new ArrayList<>(corpos);

                Iterator<Corpo> iterator = corpos2.iterator();
                while (iterator.hasNext()) {
                    Corpo c = iterator.next();
                    iterator.remove();
                    for (Corpo d : corpos2) {
                        if (colisao(c, d)) {
                            double dx = c.getTranslateX() - d.getTranslateX();
                            double dy = c.getTranslateY() - d.getTranslateY();
                            double dz = c.getTranslateZ() - d.getTranslateZ();

                            double dist2 = dx * dx + dy * dy + dz * dz;

                            double dvx = c.GetVelocidadeX() - d.GetVelocidadeX();
                            double dvy = c.GetVelocidadeY() - d.GetVelocidadeY();
                            double dvz = c.GetVelocidadeZ() - d.GetVelocidadeZ();

                            double dvDotDx = dvx * dx + dvy * dy + dvz * dz;

                            double m1 = c.getMassa();
                            double m2 = d.getMassa();

                            double fator = 2 * m2 / (m1 + m2) * (dvDotDx / dist2);

                            c.SetVelocidadeX(c.GetVelocidadeX() - fator * dx);
                            c.SetVelocidadeY(c.GetVelocidadeY() - fator * dy);
                            c.SetVelocidadeZ(c.GetVelocidadeZ() - fator * dz);

                            fator = 2 * m1 / (m1 + m2) * (dvDotDx / dist2);
                            d.SetVelocidadeX(d.GetVelocidadeX() + fator * dx);
                            d.SetVelocidadeY(d.GetVelocidadeY() + fator * dy);
                            d.SetVelocidadeZ(d.GetVelocidadeZ() + fator * dz);

                            c.getRelatorio().somarQuantidadeColisoes();
                            d.getRelatorio().somarQuantidadeColisoes();
                        }
                    }
                }
            }
        };
    }

    private boolean colisao(Corpo one, Corpo two) {
        double dx = one.getTranslateX() - two.getTranslateX();
        double dy = one.getTranslateY() - two.getTranslateY();
        double dz = one.getTranslateZ() - two.getTranslateZ();

        double distancia2 = dx * dx + dy * dy + dz * dz;
        double somaRaios = one.getRaio() + two.getRaio();
        double somaRaios2 = somaRaios * somaRaios;

        return distancia2 <= somaRaios2;
    }

}
