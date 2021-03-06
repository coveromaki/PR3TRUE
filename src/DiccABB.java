import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class NodoABB {
	private Palabra2 pal;
	private NodoABB hde;
	private NodoABB hiz;

	public NodoABB(Palabra2 p)
	{
		hde = null;
		hiz = null;
		pal = p;

	}
	public void cambiaHijoIz(NodoABB n){ 

		hiz = n;

	}
	public void cambiaHijoDe(NodoABB n){ 

		hiz = n;

	}
	public void setPalabra2(Palabra2 p){
		pal = p;
	}
	public Palabra2 getPalabra2(){
		return pal;
	}
	public NodoABB getHijoIz(){
		return hiz;
	}
	public NodoABB getHijoDe(){
		return hde;
	}
}

public class DiccABB implements Diccionario{

	private int nlenguas;
	private ArrayList<Character> lenguas; 
	private char[] lenguasaux;
	private NodoABB dicc; //raız del arbol que almacena el diccionario ordenado por la lengua origen.

	public DiccABB(){
		nlenguas = -1;
		lenguas = new ArrayList<Character>();
		dicc = null;
	}
	@Override
	public void leeDiccionario(String f){

		//definimos las variables de instancia

		FileReader fichero= null;
		BufferedReader lectura= null;

		try{ 
			//inicializamos las variables de instancia

			fichero= new FileReader(f);
			lectura= new BufferedReader(fichero);
			String linea = lectura.readLine();
			int i=0;
			while (linea!=null){
				leerLineaDicc(linea, i);
				linea=lectura.readLine(); //leemos el documento linea a linea
				i++;
			}
		}catch(IOException e){
			System.err.println("Error con el archivo");
			System.out.println(f);
		}
		//y cerramos el fichero
		try{
			if (fichero!=null)//comprobamos que fichero no sea null
				fichero.close();
			if (lectura!=null)//ni que lectura sea null
				lectura.close();
		}catch (IOException ex){//si esto falla lanzaremos la excepcion
			System.out.println(ex);
		}


	}

	public void leerLineaDicc(String texto, int linea) {

		if(linea==0){ //si es la primera linea, es donde este el numero de lenguas
			nlenguas= Integer.parseInt(texto);
			lenguasaux = new char[nlenguas];
		}
		if(linea==1){//si es la segunda, es donde estan las lenguas
			String separador= " ";
			String [] s = texto.split( separador ) ;
			for(int i=0; i<nlenguas;i++){
				lenguas.add(s[i].charAt(0)); //meto las lenguas en un vector de caracteres
				lenguasaux[i]=s[i].charAt(0); //y en un array de caracteres para  enviarsealas a las palabras2
			}

		}

		if(linea>1){
			String separador1= "[ ]*\\*[ ]*";

			String[] s = texto.split(separador1);
			Palabra2 p=new Palabra2(s[0], lenguasaux);

			//recorro el array que he creado separando la linea, menos uno, dado que 
			//origen ya la he almacenado
			for(int j=0; j<s.length-1 && j<p.getLenguas().length;j++){
				if (s[j]!=null)
				{
					//j+1 porque s[0] es la palabra origen
					p.setTrad(s[j+1], p.getLenguas()[j]);	

				}

			}
			inserta(p);

		}

	}


	@Override
	public boolean inserta(Palabra2 p) {
		// TODO Auto-generated method stub
		boolean ret = false, found = false;
		char[] lenguasPalabra;
		int comparacion = 0;
		NodoABB recorre = dicc, aux = dicc, nuevo;
		nuevo = new NodoABB(p);
		//compruebo que la palabra sea insertable
		if(p != null){
			lenguasPalabra = p.getLenguas();
			if(lenguasPalabra.length == lenguas.size()){
				for(int i=0; i<lenguasPalabra.length; i++){
					if(lenguasPalabra[i] != lenguas.get(i)){
						return false;
					}
				}
			}
		}
		else{
			return false;
		}
		//a partir de aqui el mundo es maravilloso. No existen nulos. Las lenguas son iguales. 
			if(dicc == null){
				//si es el primero, dicc sera la palabra que pongo
				dicc = nuevo;
			}
			else{
				while(recorre != null){
					comparacion = recorre.getPalabra2().getOrigen().compareToIgnoreCase(p.getOrigen());
					if(comparacion == 0){
						//la palabra es igual
						Palabra2 paux = recorre.getPalabra2();
						if(paux.combinaPalabra(p)!=-1){
							recorre.setPalabra2(paux);
							ret = true;
						}
						found = true;
						break;
					}
					else if (comparacion >0){
						//la palabra de recorre es "mayor" que la que me pasan
						//la paso a la derecha
						aux = recorre;
						recorre = recorre.getHijoIz();
					}
					else if(comparacion < 0){
						//la palabra de recorre es "menor" que la que me pasan
						//la paso a la derecha
						aux = recorre;
						recorre = recorre.getHijoDe();					
					}
				}
				if(found == false && ret == false){
					//si no la encuentro, uso la comparacion anterior
					if(comparacion > 0){
						aux.cambiaHijoIz(nuevo);
						System.out.println("insertado " + p.getOrigen() +" por la izquierda");
						ret = true;
					}
					else if(comparacion < 0){
						aux.cambiaHijoDe(nuevo);
						System.out.println("insertado " + p.getOrigen() + " por la derecha");
						ret = true;
					}
				}
			}
		
		return ret;
	}

	@Override
	public boolean borra(String s) {
		// TODO Auto-generated method stub
		//even though, borra frigthens me more than inserta
		return false;
	}

	@Override
	public int busca(String s) {
		// TODO Auto-generated method stub
		//una vez hecho borra estoy curado de espanto
		return 0;
	}

	@Override
	public String traduce(String s, char l) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void visualiza() {// :(
	}
	@Override
	public void visualiza(int j) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visualiza(int j, char l) {
		// TODO Auto-generated method stub

	}
	public void preordenABB(){
		//muestra todas las palabras del diccionario con un orden preorden
	}
	public void niveles(){
		//		muestra todas las palabras del diccionario con
		//		todas las traducciones con el formato indicado para el m�etodo escribeInfo de
		//		la clase Palabra2 seg�un el recorrido por niveles en un �arbol binario

	}
	public void camino(String s){
		//muestra el camino hacia una cadena
	}
}
