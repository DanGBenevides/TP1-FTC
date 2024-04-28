public class Estado {
    private String nome;
    private int particao;

    public Estado(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getParticao() {
        return this.particao;
    }
    public void setParticao(int particao) {
        this.particao = particao;
    }
}
