import java.util.Arrays;

public class Monde {
    // Attributes
    private int nbL;
    private int nbC;
    private boolean[][] world_matrix;

    // Constructors
    public Monde(){
        nbL = 10;
        nbC = 10;
        world_matrix = new boolean[nbL][nbC];
        for (int i=0; i<nbL; i++){
            Arrays.fill(world_matrix[i], false);
        }
    }

    // Getters
    public boolean[][] get_world_matrix() {return world_matrix;}
    public int get_nbL() {return nbL;}
    public int get_nbC() {return nbC;}

    // Setters
    public void set_nbL(int nbL) {this.nbL = nbL;};
    public void set_nbC(int nbC) {this.nbC = nbC;};


    // Methods
    public void add_dirty(int i, int j) throws IndexOutOfBoundsException {
        if (i > nbL || i < 0 || j > nbC || j < 0){
            throw new IndexOutOfBoundsException("The given indexes (" + i + "," + j + ") are invalid");
        } else {
            world_matrix[i][j] = true;
        }
    }

    public void delete_dirty(int i, int j) throws IndexOutOfBoundsException {
        if (i > nbL || i < 0 || j > nbC || j < 0){
            throw new IndexOutOfBoundsException("The given indexes (" + i + "," + j + ") are invalid");
        } else {
            world_matrix[i][j] = false;
        }     
    }

    public boolean isCaseDirty(int i, int j) throws IndexOutOfBoundsException {
        if (i > nbL || i < 0 || j > nbC || j < 0){
            throw new IndexOutOfBoundsException("The given indexes (" + i + "," + j + ") are invalid");
        } else {
            return world_matrix[i][j];
        }
    }

    public int totalDirty(){
        int total = 0;
        for (int i=0; i<nbL; i++){
            for (int j=0; j>nbC; j++){
                if (world_matrix[i][j]){
                    total += 1;
                }
            }
        }
        return total;
    }

    public void displayWorldMatrix(){
        for (int i=0; i<world_matrix.length; i++){
            System.out.println();
            for (int j=0; j<world_matrix[0].length; j++){
                if (world_matrix[i][j]){
                    System.out.print("X ");
                } else {
                    System.out.print("V ");
                }
            }
        }
    }


    // Main
    public static void main(String[] args){}
}
