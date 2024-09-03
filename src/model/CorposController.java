package model;

public class CorposController {
    public static final double G = 120e-2;
    public void atualizarPosicao(Corpo one, Corpo two) {
        Double forca = (one.getMassa() * two.getMassa()) * G / Distancia(one, two);
        Double aceleracao_one = forca / one.getMassa();
        Double aceleracao_two = forca / two.getMassa();

        if (one.getTranslateX() < two.getTranslateX()) {
            one.SetVelocidadeX(one.GetVelocidadeX() + aceleracao_one);
            two.SetVelocidadeX(two.GetVelocidadeX() - aceleracao_two);
        } else if (one.getTranslateX() > two.getTranslateX()) {
            one.SetVelocidadeX(one.GetVelocidadeX() - aceleracao_one);
            two.SetVelocidadeX(two.GetVelocidadeX() + aceleracao_two);
        }

        if (one.getTranslateY() < two.getTranslateY()) {
            one.SetVelocidadeY(one.GetVelocidadeY() + aceleracao_one);
            two.SetVelocidadeY(two.GetVelocidadeY() - aceleracao_two);
        } else if (one.getTranslateY() > two.getTranslateY()) {
            one.SetVelocidadeY(one.GetVelocidadeY() - aceleracao_one);
            two.SetVelocidadeY(two.GetVelocidadeY() + aceleracao_two);
        }

        if (one.getTranslateZ() < two.getTranslateZ()) {
            one.SetVelocidadeZ(one.GetVelocidadeZ() + aceleracao_one);
            two.SetVelocidadeZ(two.GetVelocidadeZ() - aceleracao_two);
        } else if (one.getTranslateZ() > two.getTranslateZ()) {
            one.SetVelocidadeZ(one.GetVelocidadeZ() - aceleracao_one);
            two.SetVelocidadeZ(two.GetVelocidadeZ() + aceleracao_two);
        }

        one.setTranslateX(one.getTranslateX() + one.GetVelocidadeX());
        one.setTranslateZ(one.getTranslateZ() + one.GetVelocidadeZ());
        one.setTranslateY(one.getTranslateY() + one.GetVelocidadeY());
        two.setTranslateX(two.getTranslateX() + two.GetVelocidadeX());
        two.setTranslateZ(two.getTranslateZ() + two.GetVelocidadeZ());
        two.setTranslateY(two.getTranslateY() + two.GetVelocidadeY());

    }

    private double DistanciaX(Corpo one, Corpo two) {
        double distancia = one.getTranslateX() - two.getTranslateX();
        return distancia;
    }

    private double DistanciaY(Corpo one, Corpo two) {
        double distancia = one.getTranslateY() - two.getTranslateY();
        return distancia;
    }

    private double Distancia(Corpo one, Corpo two) {
        double x = DistanciaX(one, two);
        double y = DistanciaY(one, two);
        return Math.sqrt((x * x) + (y * y));
    }
}
