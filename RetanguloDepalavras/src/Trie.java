import java.util.HashMap;
import java.util.HashSet;

public class Trie {

    private TrieNode raiz;

    public Trie() {
        this.raiz = new TrieNode(null);
    }

    public void adicionaPalavra(String palavra) {
        TrieNode noCorrente = this.raiz;
        for (char c : palavra.toCharArray()) {
            TrieNode noFilho = noCorrente.getFilho(c);
            if (noFilho == null) {
                noFilho = noCorrente.addFilho(c);
            }
            noFilho.tamanhosPrefixo.add(palavra.length());
            noCorrente = noFilho;
        }
        noCorrente.setTerminal(true);
    }

    public boolean temPrefixo(String prefixo, int tamanho) {
        TrieNode no = obterUltimoNoPrefixo(prefixo, tamanho);
        return no != null;
    }

    private TrieNode obterUltimoNoPrefixo(String prefixo, int tamanho) {
        TrieNode noCorrente = this.raiz;
        for (char c : prefixo.toCharArray()) {
            TrieNode noFilho = noCorrente.getFilho(c);
            if (noFilho == null) {
                return null;
            }
            noCorrente = noFilho;
        }
        if (noCorrente.tamanhosPrefixo.contains(tamanho)){
        	return noCorrente;
        }
        else {
        	return null;
        }        	
    }

    public boolean temPalavra(String palavra) {
        TrieNode no = obterUltimoNoPrefixo(palavra, palavra.length());
        return no != null && no.isTerminal();
    }

    private class TrieNode {

        private final Character caracter;
        boolean terminal;
        HashSet<Integer> tamanhosPrefixo;
        HashMap<Character, TrieNode> filhos;

        TrieNode(Character caracter) {
            this.caracter = caracter;
            this.terminal = false;
            this.tamanhosPrefixo = new HashSet<Integer>();
            this.filhos = new HashMap<>();
        }

        public Character getCaracter() {
            return caracter;
        }

        public boolean isTerminal() {
            return terminal;
        }

        public void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }

        public TrieNode getFilho(char caracter) {
            return this.filhos.get(caracter);
        }

        public TrieNode addFilho(char caracter) {
            TrieNode filhoNode = new TrieNode(caracter);
            this.filhos.put(caracter, filhoNode);
            return filhoNode;
        }
    }
}
