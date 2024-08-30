package model;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Corpo extends Sphere {
    private Double massa;
    private double x;
    private double y;
    private double densidade;
    private double volume;
    private String nome;
    private double raio;
    private double velocidadeX;
    private double velocidadeY;
    private double velocidadeZ;
    private PhongMaterial material = new PhongMaterial();
 
    
    public Double getMassa() {
        return massa;
    }

    public void setMassa(Double massa) {
        this.massa = massa;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDensidade() {
        return densidade;
    }

    public void setDensidade(double densidade) {
        this.densidade = densidade;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getNome() {
        return nome;
    }

    public double GetVelocidadeX(){
        return velocidadeX;
    }

    public double GetVelocidadeY(){
        return velocidadeY;
    }

    public void SetVelocidadeX(double velocidadex){
        this.velocidadeX = velocidadex;
    }

    public void SetVelocidadeY(double velocidadey){
        this.velocidadeY = velocidadey;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    public Corpo(Double massa, String nome, double raio, double x, double y, double z, double velocidadex, double velociadey, double velociadez){
        this.x = x;
        this.setTranslateX(x);
        this.y = y;
        this.setTranslateY(y);
        this.setTranslateZ(z);
        this.massa = massa;
        this.nome = nome;
        this.raio = raio;
        this.setRadius(raio);
        this.velocidadeX = velocidadex;
        this.velocidadeY = velociadey;
        this.velocidadeZ = velociadez;
        volume = (float)((4/3)*3.14*(raio*raio*raio));
        densidade = (float)(massa/volume);
        
    }

    public Corpo(){

    }

    public void Colorir(String cor){
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.valueOf(cor));
        this.setMaterial(material);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double GetVelocidadeZ() {
        return velocidadeZ;
    }

    public void Texturizar(String local){
        material.setDiffuseMap(new Image(getClass().getResourceAsStream(local)));
        this.setMaterial(material);
    }

    public void SetVelocidadeZ(double velocidadeZ) {
        this.velocidadeZ = velocidadeZ;
    }
}

