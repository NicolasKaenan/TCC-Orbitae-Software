package model;

public class Relatorio {
    private int idRelatorio;
    private int idCorpo;
    private String nomeCorpo;
    private double massa;
    private double densidade;
    private double volume;
    private double raio;
    private int quantidadeColisoes;
    private double velocidadeMediaX;
    private double velocidadeMediaY;
    private double velocidadeMediaZ;
    private String cor;
    private int x_contador, y_contador, z_contador = 0;

    public Relatorio(){

    }
    
    public Relatorio(Corpo corpo) {
        this.idCorpo = corpo.GetId();
        this.nomeCorpo = corpo.getNome();
        this.massa = corpo.getMassa();
        this.densidade = corpo.getDensidade();
        this.volume = corpo.getVolume();
        this.raio = corpo.getRaio();
        this.velocidadeMediaX = corpo.getVelocidadeX();
        this.velocidadeMediaY = corpo.getVelocidadeY();
        this.velocidadeMediaZ = corpo.getVelocidadeZ();
        this.cor = corpo.getCor();
        this.quantidadeColisoes = 0; 
    }



    public int getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(int idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public int getIdCorpo() {
        return idCorpo;
    }

    public void setIdCorpo(int idCorpo) {
        this.idCorpo = idCorpo;
    }

    public String getNomeCorpo() {
        return nomeCorpo;
    }

    public void setNomeCorpo(String nomeCorpo) {
        this.nomeCorpo = nomeCorpo;
    }

    public double getMassa() {
        return massa;
    }

    public void setMassa(double massa) {
        this.massa = massa;
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

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }

    public int getQuantidadeColisoes() {
        return quantidadeColisoes;
    }

    public void setQuantidadeColisoes(int quantidadeColisoes) {
        this.quantidadeColisoes = quantidadeColisoes;
    }

    public void somarQuantidadeColisoes(){
        this.quantidadeColisoes += 1;
    }

    public double getVelocidadeMediaX() {
        return velocidadeMediaX;
    }

    public void setVelocidadeMediaX(double velocidadeMediaX) {
        this.velocidadeMediaX = velocidadeMediaX;
    }

    public double getVelocidadeMediaY() {
        return velocidadeMediaY;
    }

    public void setVelocidadeMediaY(double velocidadeMediaY) {
        this.velocidadeMediaY = velocidadeMediaY;
    }

    public double getVelocidadeMediaZ() {
        return velocidadeMediaZ;
    }

    public void setVelocidadeMediaZ(double velocidadeMediaZ) {
        this.velocidadeMediaZ = velocidadeMediaZ;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void AddVelocidadeMediaX(double velociade_x){
        velocidadeMediaX = (velocidadeMediaX*x_contador)+velociade_x/(double)(x_contador+1);
        x_contador++;
    }

    public void AddVelocidadeMediaY(double velociade_Y){
        velocidadeMediaY = (velocidadeMediaY*y_contador)+velociade_Y/(double)(y_contador+1);
        y_contador++;
    }

    public void AddVelocidadeMediaZ(double velociade_Z){
        velocidadeMediaZ = (velocidadeMediaZ*z_contador)+velociade_Z/(double)(z_contador+1);
        z_contador++;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "idRelatorio=" + idRelatorio +
                ", idCorpo=" + idCorpo +
                ", nomeCorpo='" + nomeCorpo + '\'' +
                ", massa=" + massa +
                ", densidade=" + densidade +
                ", volume=" + volume +
                ", raio=" + raio +
                ", quantidadeColisoes=" + quantidadeColisoes +
                ", velocidadeMediaX=" + velocidadeMediaX +
                ", velocidadeMediaY=" + velocidadeMediaY +
                ", velocidadeMediaZ=" + velocidadeMediaZ +
                ", cor='" + cor + '\'' +
                '}';
    }
}
