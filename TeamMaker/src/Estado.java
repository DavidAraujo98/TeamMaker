public enum Estado {

    ATIVO("Ativo"), INATIVIDADE("Inatividade"), SUSPENSAO("Suspensao");

    private String x = null;

    public String getValue() {
        return this.x;
    }

    private Estado(String x) {
        this.x = x;
    }
}