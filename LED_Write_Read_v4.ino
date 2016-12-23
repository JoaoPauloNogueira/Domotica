/*************************************************
 * CÓDIGO ARDUINO
 * ALUNOS: JOÃO PAULO E ANTONIO DE LISBOA
 * DISCIPLINA: RESIDÊNCIA DE SOFTWARE I
 * PROFESSOR: BRUNO
 * GitHub:
 ************************************************/

/******* CONSTANTES E VARIÁVEIS **********/
char command;
String string;
boolean ledVon = false;
boolean ledAon = false;
boolean ledDon = false;

/******* PORTAS DOS LEDS ******************/
#define ledV 13     // Led vermelho
#define ledA 12     // Led amarelo
#define ledVe 11    // Led verde

/******* Sensor de temperatura usando o LM35 **************************/
const int LM35 = A0;     // Define o pino que lera a saída do LM35
float temperatura;       // Variável que armazenará a temperatura medida
int temperaturaInt;

void setup() {
    Serial.begin(9600);
    pinMode(ledV, OUTPUT);
    pinMode(ledA, OUTPUT);
    pinMode(ledVe, OUTPUT);
}

void loop() {
  
    /*  Serial.available()
     *  Retorna a quantidades de bytes disponíveis para leitura no 
     *  buffer de leitura. Essa função auxilia em loops onde a leitura 
     *  dos dados só e realizada quando há dados disponível. 
     *  A quantidade máxima de bytes no buffer é 64.
     *******************************************************************/
    if (Serial.available() > 0){
      string = "";
    }
    
    while(Serial.available() > 0){
      command = ((byte)Serial.read());  // Serial.read() - Lê o byte mais recente apontado no buffer de entrada da serial.
      
      if(command == ':'){
        break;
      }
      else{
        string += command;
      }
      delay(1);
    }
    
 /******************** Led vermelho *********************/   
    if(string == "VO"){ ledVon = true; }
    if(string =="VF"){ ledVon = false; }
    
 /*******************************************************/  

 /******************** Led amarelo **********************/   
    if(string == "AO"){ ledAon = true; }
    if(string =="AF"){ ledAon = false; }
    
 /*******************************************************/ 

 /******************** Led verde ************************/   
    if(string == "DO"){ ledDon = true; }
    if(string =="DF"){ ledDon = false; }
   
 /*******************************************************/ 

 /****** Guarda a informação do LED ligado **************/
  if (ledVon) {
    ledOn(ledV);
  } else {
    ledOff(ledV);
  }

  if (ledAon) {
    ledOn(ledA);
  } else {
    ledOff(ledA);
  }
  if (ledDon) {
    ledOn(ledVe);
  } else {
    ledOff(ledVe);
  }

  /*******************************************************/ 
  
  //Função leitura e escrita da temperatura
  temperatura = (float(analogRead(LM35))*5/(1023))/0.01;      // Calcula a temperatura
  temperaturaInt = temperatura / 100;                         // Transformação das casas decimais da temperatura
  Serial.write(temperaturaInt);                               // Escreve informação na porta SERIAL
  Serial.println(temperaturaInt);                             // Escreve informação no Monitor Serial
  delay(10);

  
}  /***************** Fim do loop******************/
 
void ledOn(int porta) {
      analogWrite(porta, 255);
      delay(10);
}
 
void ledOff(int porta){
      analogWrite(porta, 0);
      delay(10);
}
 

    
