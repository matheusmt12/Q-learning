import java.beans.VetoableChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        char[] acao = new char[4];
        acao[0] = 'w';
        acao[1] = 's';
        acao[2] = 'a';
        acao[3] = 'd';
        vertice atual, ant;
        float valor;
        int cont = 0;
        int linha = 0;
        int coluna = 0;
        int antl = 0;
        int antc = 0;
        String obstaculo = "1";
        Random random = new Random();

        // Alocando memoria na matriz
        vertice[][] matriz = new vertice[3][3];
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                matriz[i][j] = new vertice();
            }
        }

        String[][] matrizAux = { { "0", "0", "0" },
                { "0", "0", "0" },
                { "0", "0", "0" } };

        String[][] matrizObs = { { "0", "0", "0" },
                { "1", "0", "1" },
                { "1", "0", "0" } };

        // Carregando o estado

        System.out.println("Escolha o Ambiente 01 ou 02 \n");
        System.out.println();
        System.out.println("Ambiente 01");
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print(" " + matrizAux[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println("Ambiente 02");
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                System.out.print("  " + matrizObs[i][j] + " ");
            }
            System.out.println();

        }

        Scanner ler = new Scanner(System.in);

        System.out.println("Escolha o ambiente que deseja operar");
        int num1 = ler.nextInt();
        
                if (num1 == 1){
                
                   for (int i = 0; i <= 2; i++) {
                        for (int j = 0; j <= 2; j++) {
                            matriz[i][j].setEstado(matrizAux[i][j]);
                        }
                    } 
                }else{
                   for (int i = 0; i <= 2; i++) {
                        for (int j = 0; j <= 2; j++) {
                            matriz[i][j].setEstado(matrizObs[i][j]);
                        }
                    }                   
                }
   

 



        /*
         * for (int i = 0; i < 3; i++) {
         * for (int j = 0; j < 3; j++) {
         * System.out.print(matriz[i][j].getEstado());
         * }
         * System.out.println();
         * }
         */
        matriz[2][2].setReforco(10);
        matriz[2][2].setBoneco("A");

        while (cont < 1000) {
            int numAle = (int) (Math.random() * 4);

            if (acao[numAle] == 'w') {
                if (linha > 0) {

                    antl = linha;
                    antc = coluna;
                    linha = linha - 1;
                    if (linha < 0) {
                        linha = linha + 1;
                        continue;
                    }
                    if (!matriz[linha][coluna].getEstado().equals(obstaculo)) {
                        atual = matriz[linha][coluna];
                        valor = QL(atual);
                        matriz[antl][antc].setW(valor);
                    }

                }
            } else if (acao[numAle] == 'a') {
                if (coluna > 0) {

                    antl = linha;
                    antc = coluna;
                    coluna = coluna - 1;
                    if (coluna < 0) {
                        coluna = coluna + 1;
                        continue;
                    }
                    if (!matriz[linha][coluna].getEstado().equals(obstaculo)) {
                        atual = matriz[linha][coluna];
                        valor = QL(atual);
                        matriz[antl][antc].setA(valor);
                    }

                }

            }

            else if (acao[numAle] == 'd') {
                if (coluna < 2) {

                    antl = linha;
                    antc = coluna;
                    coluna = coluna + 1;
                    if (coluna >= 3) {
                        coluna = coluna - 1;
                        continue;
                    }
                    if (!matriz[linha][coluna].getEstado().equals(obstaculo)) {
                        atual = matriz[linha][coluna];
                        valor = QL(atual);
                        matriz[antl][antc].setD(valor);
                    }
                }
            } else if (acao[numAle] == 's') {
                if (linha < 2) {

                    antl = linha;
                    antc = coluna;
                    linha = linha + 1;
                    if (linha >= 3) {
                        linha = linha - 1;
                        continue;
                    }
                    if (!matriz[linha][coluna].getEstado().equals(obstaculo)) {
                        atual = matriz[linha][coluna];
                        valor = QL(atual);
                        matriz[antl][antc].setS(valor);
                    }

                }
            }

            cont++;
        }

        /*
         * for(int i = 0; i < 3; i++){
         * for (int j = 0; j < 3;j++){
         * System.out.print(matriz[i][j].getW() + ";");
         * System.out.print(matriz[i][j].getS()+ ";");
         * System.out.print(matriz[i][j].getA()+ ";");
         * System.out.print(matriz[i][j].getD()+ ";");
         * }
         * System.out.println();
         * }
         */

        vertice teste = new vertice();
        matriz[2][2].setEstado("A");
        navegar(matriz, "A", 0, 0);

        File arquivo = new File("file.txt");
        arquivo.createNewFile();
        FileWriter fw = new FileWriter(arquivo);
        BufferedWriter write = new BufferedWriter(fw);
        
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                write.write(" " + matriz[i][j].getEstado() + " ");
            }
            write.write("\n");
        }

        write.close();
        fw.close();

        System.out.println("Caminho Perorrido no esta no arquivo");

    }

    public static float QL(vertice aux) {
        float max = maximo(aux), g = 0.9f;
        float reforco = aux.getReforco();

        return reforco + (g * max);

    }

    public static float maximo(vertice max) {
        float cont = 0.0f;

        if (cont < max.getA())
            cont = max.getA();
        if (cont < max.getD())
            cont = max.getD();
        if (cont < max.getS())
            cont = max.getS();
        if (cont < max.getW())
            cont = max.getW();
        return cont;

    }

    public static void navegar(vertice matriz[][], String boneco, int li, int co) {
        int linha = li;
        int coluna = co;
        matriz[li][co].setEstado("A");
        matriz[2][2].setEstado("_");

        while (!matriz[2][2].getEstado().equals(boneco)) {
            if (matriz[linha][coluna].getW() >= matriz[linha][coluna].getA()
                    && matriz[linha][coluna].getW() >= matriz[linha][coluna].getD() &&
                    matriz[linha][coluna].getW() >= matriz[linha][coluna].getS()) {
                if (linha <= 0) {
                    continue;
                }
                linha = linha - 1;
                matriz[linha][coluna].setEstado(boneco);
            } else if (matriz[linha][coluna].getS() >= matriz[linha][coluna].getA()
                    && matriz[linha][coluna].getS() >= matriz[linha][coluna].getD() &&
                    matriz[linha][coluna].getS() >= matriz[linha][coluna].getW()) {
                if (linha >= 3) {
                    continue;
                }
                linha = linha + 1;
                matriz[linha][coluna].setEstado(boneco);
            } else if (matriz[linha][coluna].getA() >= matriz[linha][coluna].getW()
                    && matriz[linha][coluna].getA() >= matriz[linha][coluna].getD() &&
                    matriz[linha][coluna].getA() >= matriz[linha][coluna].getS()) {
                if (coluna < 0) {
                    continue;
                }
                coluna = coluna - 1;
                matriz[linha][coluna].setEstado(boneco);
            }
            if (matriz[linha][coluna].getD() >= matriz[linha][coluna].getD()
                    && matriz[linha][coluna].getD() >= matriz[linha][coluna].getA() &&
                    matriz[linha][coluna].getD() >= matriz[linha][coluna].getS()) {
                if (coluna >= 3) {
                    continue;
                }
                coluna = coluna + 1;
                matriz[linha][coluna].setEstado(boneco);
            }

        }
        vertice aux = new vertice();

    }

}
