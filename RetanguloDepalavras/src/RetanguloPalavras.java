import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RetanguloPalavras {

    /**
     * Todas as palavras validas
     */
    private List<String> palavrasValidas;

    /**
     * Na posicao j, armazena uma lista com todas
     * as palavras validas de tamanho j
     */
    private List<List<String>> palavrasPorTamanho;

    /**
     * Armazena uma trie com todas
     * as palavras validas do arquivo,
     * independente de tamanho
     */
    private Trie triePrincipal;

    public RetanguloPalavras(List<String> palavrasValidas) {
        this.palavrasValidas = palavrasValidas;
        this.triePrincipal = new Trie();

        this.palavrasPorTamanho = new ArrayList<>();

        // separa as palavras por tamanho
        for (String palavra : palavrasValidas) {
            int tamanho = palavra.length();
            /* expande os arrays, se necessario,
               ate conter a posicao desejada */
            while (this.palavrasPorTamanho.size() <= tamanho) {
                this.palavrasPorTamanho.add(null);
            }
            List<String> lista = this.palavrasPorTamanho.get(tamanho);
            if (lista == null) {  // lazy instantiation
                lista = new ArrayList<>();
                this.palavrasPorTamanho.set(tamanho, lista);
            }
            triePrincipal.adicionaPalavra(palavra);
            lista.add(palavra);
        }
    }

    private boolean backtrack(
            List<String> estado, int tamanho) {

        // verifica se eh estado final (solucao)
        if (estado.size() == tamanho) {
            return true;
        }

        List<String> palavras = this.palavrasPorTamanho.get(tamanho);

        for_candidatas:
        for (String candidata : palavras) {

            // verifica se pode ser incluida (se os prefixos existem)
            for (int coluna_idx = 0; coluna_idx < tamanho; coluna_idx++) {
                StringBuffer sb = new StringBuffer();
                for (String linha : estado) {
                    sb.append(linha.substring(
                            coluna_idx, coluna_idx + 1));
                }
                sb.append(candidata.substring(
                        coluna_idx, coluna_idx + 1));
                String prefixo = sb.toString();

                if (!triePrincipal.temPrefixo(prefixo, tamanho)) {
                    continue for_candidatas;  // essa candidata nao serve (poda!)
                }
            }

            // candidata ok!
            estado.add(candidata);
            if (backtrack(estado, tamanho)) {
                return true;  // repassando para cima o fato de ter
                              // encontrado um estado final
            }
            estado.remove(estado.size() - 1);
        }

        return false;
    }

    public List<String> encontraRetangulo(int tamanho) {
        if (tamanho >= this.palavrasPorTamanho.size()) {
            return null;
        }
        if (this.palavrasPorTamanho.get(tamanho) == null) {
            return null;
        }
        List<String> estado = new ArrayList<>();
        return backtrack(estado, tamanho) ? estado : null;
    }

    public static List<String> lerPalavras(String filePath) {
        List<String> resultado = new ArrayList<>();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            resultado.add(scanner.next());
        }
        scanner.close();

        return resultado;
    }

    public static void main(String args[]) {
    	System.out.print("Lendo arquivo...");
        List<String> listaPalavras = lerPalavras("D:/Dropbox/TEP/words.txt");
        System.out.println("Terminado!");
        int k = 5;

        RetanguloPalavras solver = new RetanguloPalavras(listaPalavras);

        System.out.println("Procurando solução...");
        long start = System.currentTimeMillis();
        List<String> resultado = solver.encontraRetangulo(k);
        long elapsed = System.currentTimeMillis() - start;

        if (resultado != null) {
            for (String palavra : resultado) {
                System.out.println(palavra);
            }
        } else {
            System.out.println("Sem solução.");
        }
        System.out.println("Tempo da busca: " + elapsed + "ms");
    }
}
