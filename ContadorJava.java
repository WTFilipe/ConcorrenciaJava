class SomadorDeVetor {
	private int soma;
	private int[] vetor;
	
	public SomadorDeVetor(int[] vetor){
		this.vetor = vetor;
		soma = 0;
	}
	
	public int getValorPorIndex(int index){
		return vetor[index];
	}
	
	public synchronized void soma(int valor){
		this.soma += valor;
	}
	
	public int getSoma() {
		return this.soma;
	}
}

class T extends Thread {
   private int id;
   private int indexPorThread;
   private SomadorDeVetor somaDeVetor;
   private int tamanhoDoVetor;
  
   public T(int tid, SomadorDeVetor somaDeVetor, int indexPorThread, int tamanhoDoVetor) { 
      this.id = tid; 
      this.somaDeVetor = somaDeVetor;
      this.indexPorThread = indexPorThread;
      this.tamanhoDoVetor = tamanhoDoVetor;
   }

   public void run() {
      int primeiroIndex = id * tamanhoDoVetor;
      int ultimoIndex = primeiroIndex + tamanhoDoVetor;
      
      if(ultimoIndex > tamanhoDoVetor) {
      	ultimoIndex = tamanhoDoVetor;
      }
      for(int i = primeiroIndex; i < ultimoIndex; i++){
      	somaDeVetor.soma(somaDeVetor.getValorPorIndex(i));
      }
   }
}

class ContadorJava {
   static final int NUMERO_DE_THREADS = 4;
   static final int TAMANHO_DO_VETOR = 10000;

   public static void main (String[] args) {
      Thread[] threads = new Thread[NUMERO_DE_THREADS];
      int[] vetor = new int[TAMANHO_DO_VETOR];

      SomadorDeVetor somadorDeVetor = new SomadorDeVetor(vetor);
	
      for(int i = 0; i<TAMANHO_DO_VETOR; i++){
      	vetor[i] = i % 13;
      }
      int indexPorThread = TAMANHO_DO_VETOR/NUMERO_DE_THREADS;
      
      //cria as threads da aplicacao
      for (int i=0; i<threads.length; i++) {
         threads[i] = new T(i, somadorDeVetor, indexPorThread, TAMANHO_DO_VETOR);
      }

      //inicia as threads
      for (int i=0; i<threads.length; i++) {
         threads[i].start();
      }

      //espera pelo termino de todas as threads
      for (int i=0; i<threads.length; i++) {
         try { threads[i].join(); } catch (InterruptedException e) { return; }
      }
      
      //Realiza soma sequencial para verificar corretude
      SomadorDeVetor somadorSequencial = new SomadorDeVetor(vetor);
      for(int i = 0; i < TAMANHO_DO_VETOR; i++){
      	somadorSequencial.soma(somadorSequencial.getValorPorIndex(i));
      }
      
      if(somadorSequencial.getSoma() == somadorDeVetor.getSoma()){
        System.out.println("Valor da soma = " + somadorDeVetor.getSoma()); 
      	System.out.println("Algoritmo concorrente está CORRETO");
      } else {
      	System.out.println("Valor da soma concorrente= " + somadorDeVetor.getSoma());
      	System.out.println("Valor da soma sequencial= " + somadorSequencial.getSoma()); 
      	System.out.println("Algoritmo concorrente está INCORRETO");
      } 
   }
}
