package com.example.almohadascomodasademsbonitas.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.example.almohadascomodasademsbonitas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import androidx.recyclerview.widget.LinearSnapHelper;


public class actividad_pedido extends AppCompatActivity {
    private int dia,mes,anio;
    private int imagenSeleccionada = 0;
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private String nombreComercialElegido;
    EditText editText;
    TextView tvfecha;
    RecyclerView rvLista;
    private TextView TextConf;  // Añade esta línea
    private ArrayList<String>mImagesUrls = new ArrayList<>();
    private ArrayList<String>mNames = new ArrayList<>();
    String nombrePartnerelegido;
    ArrayList<HashMap<String, String>> comerciales;
    ArrayList<HashMap<String, String>> partners;

    //Estas dos linea son una prueba
    int comercialEleguido=0;
    int partnerEleguido=0;

    Button buttonComprar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos_actividad);

        comerciales = new ArrayList<>();
        partners = new ArrayList<>();

        tvfecha = findViewById(R.id.textViewfecha);
        editText = findViewById(R.id.editTextText3);

        TextConf = findViewById(R.id.textView6);

        beginXMLparsingComerciales();
        beginXMLparsingPartners();


        // Obtén los recursos de la carpeta drawable
        String nomb = "jordi";

        int[] arrayDrawableResources = new int[]{
                R.drawable.jordi,
                R.drawable.bale,
                R.drawable.bob,
                R.drawable.cartas,
                R.drawable.hello,
                R.drawable.patriota,
                R.drawable.pistola,
                R.drawable.verde
        };

        // Rellena mImagesUrls y mNames con los recursos de la carpeta drawable
        for (int drawableResource : arrayDrawableResources) {
            mImagesUrls.add(String.valueOf(drawableResource));

            // Puedes obtener el nombre del recurso también (sin la extensión)
            String resourceName = getResources().getResourceEntryName(drawableResource);
            mNames.add(resourceName);
        }

        getRecycleView();


        muestrafecha();

        // ...

        rvLista.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int itemPosition = rvLista.getChildLayoutPosition(rvLista.findChildViewUnder(event.getX(), event.getY()));

                    /*rvLista.findChildViewUnder(event.getX(), event.getY()): Este método encuentra la vista del hijo
                    en el RecyclerView que está ubicada en las coordenadas especificadas. En otras palabras, determina
                    qué elemento del RecyclerView fue tocado en la posición (event.getX(), event.getY()).

                    rvLista.getChildLayoutPosition(...): Este método obtiene la posición del adaptador del
                    elemento en el RecyclerView. El resultado es la posición del elemento que fue tocado en la lista.*/


                    if (itemPosition != RecyclerView.NO_POSITION) {
                        // Aquí actualizas la variable imagenSeleccionada
                        imagenSeleccionada = itemPosition;
                        // No llames al método incrementarNumeroEnTextView() aquí
                    }
                }
                return false;
            }
        });

        Button buttonConfirmarImagen = findViewById(R.id.buttonConfirmar);
        buttonConfirmarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica si el valor del EditText es 0
                if (editText.getText().toString().equals("0")) {
                    // Muestra un Toast indicando que no se ha elegido ningún producto
                    Toast.makeText(actividad_pedido.this, "No has elegido ningún producto.", Toast.LENGTH_SHORT).show();
                } else {
                    // Llama al método incrementarNumeroEnTextView solo cuando se confirma la selección
                    incrementarNumeroEnTextView();

                    // Cambia el valor del EditText a 0
                    editText.setText("0");
                }
            }
        });



// ...



        Button buttonBorrar = findViewById(R.id.buttonBorrar);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vacía el array de pedidos
                listaPedidos.clear();

                // Establece el valor de textView6 a "0"
                TextConf.setText("0");
                editText.setText("0");
            }
        });

        rvLista = findViewById(R.id.lista);

        // Agrega un OnTouchListener al RecyclerView para manejar clics



        buttonComprar = findViewById(R.id.buttonComprar);

        buttonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica si el valor de TextConf es 0
                if (TextConf.getText().toString().equals("0")) {
                    // Muestra un Toast indicando que no se han elegido productos
                    Toast.makeText(actividad_pedido.this, "No has elegido ningún producto.", Toast.LENGTH_SHORT).show();
                } else {
                    // El valor de TextConf no es 0, procede con la acción deseada
                    // En este caso, envía la lista de pedidos a la nueva actividad
                    Intent intent = new Intent(actividad_pedido.this, com.example.almohadascomodasademsbonitas.pedidos.pagina2pedido.class);
                    intent.putExtra("pedidos", listaPedidos);
                    intent.putExtra("fecha",tvfecha.getText().toString());
                    intent.putExtra("partners",nombrePartnerelegido);
                    intent.putExtra("comerciales",nombreComercialElegido);
                    intent.putExtra("pro_elegido",TextConf.getText().toString());
                    startActivity(intent);
                }
            }
        });


    }

    private void incrementarNumeroEnTextView() {
        Log.d("actividad_pedido", "Imagen seleccionada en incrementarNumeroEnTextView(): " + imagenSeleccionada);

        // Obtén la cantidad actual del TextView6
        String cantidadActual = TextConf.getText().toString();

        // Verifica si la cadena está vacía
        if (!cantidadActual.isEmpty()) {
            // Convierte la cantidad actual a un entero
            int cantidad = Integer.parseInt(cantidadActual);

            // Incrementa la cantidad en uno
            cantidad++;

            // Verifica si la cantidad alcanzó 100 y reinicia a 1 si es necesario
            if (cantidad > 100) {
                cantidad = 1;
            }

            // Actualiza el TextView6 con la nueva cantidad
            TextConf.setText(String.valueOf(cantidad));

            // Obtén el nombre del producto seleccionado según la imagen
            String nombreProducto = obtenerNombreProductoDesdeImagen(imagenSeleccionada);
            int precioProducto = obtenerprecioArticulo(nombreProducto);
            // Obtén la cantidad del EditText
            String cantidadEditText = editText.getText().toString();

            // Verifica si la cadena del EditText no está vacía
            if (!cantidadEditText.isEmpty()) {
                // Convierte la cantidad del EditText a un entero
                int cantidadPedido = Integer.parseInt(cantidadEditText);

                 /*Si la lista de pedidos está vacía, se asigna el valor 1 a contadorIdPedido.
                Si la lista no está vacía, se toma el último elemento de la lista (listaPedidos.get(listaPedidos.size() - 1))
                y se obtiene su ID de pedido (getIdPedido()). Luego, se suma 1 a ese ID y se asigna el resultado a contadorIdPedido.*/
                int contadorIdPedido = (listaPedidos.isEmpty()) ? 1 : listaPedidos.get(listaPedidos.size() - 1).getIdPedido() + 1;


                // Crea un objeto Pedido y agrégalo al ArrayList
                Pedido nuevoPedido = new Pedido(nombreProducto, cantidadPedido, contadorIdPedido,precioProducto);

                listaPedidos.add(nuevoPedido);

                // Imprime información del nuevo pedido (ajústalo según tus necesidades)
                Log.d("actividad_pedido", "Nuevo pedido: Imagen: " + nombreProducto + ", Cantidad: " + nuevoPedido.getCantidad());
            } else {
                // Manejo de caso cuando el EditText está vacío
                TextConf.setText("0"); // O cualquier otro valor predeterminado que desees
            }
        } else {
            // Manejo de caso cuando el TextView6 está vacío
            TextConf.setText("0"); // O cualquier otro valor predeterminado que desees
        }
    }


    // Método para obtener el nombre del producto según la imagen seleccionada
    private String obtenerNombreProductoDesdeImagen(int imagenSeleccionada) {
        switch (imagenSeleccionada) {
            case 0:
                return "jordi";
            case 1:
                return "bale";
            case 2:
                return "bob";
            case 3:
                return "cartas";
            case 4:
                return "hello";
            case 5:
                return "patriota";
            case 6:
                return "pistola";
            case 7:
                return "verde";
            default:
                return "";
        }
    }

    private int obtenerprecioArticulo(String nombre) {
        switch (nombre) {
            case "jordi":
                return 30;
            case "bale":
                return 30;
            case "bob":
                return 30;
            case "cartas":
                return 30;
            case "hello":
                return 30;
            case "patriota":
                return 30;
            case "pistola":
                return 30;
            case "verde":
                return 30;
            default:
                return 0;
        }
    }



    private void muestrafecha() {
        Calendar fecha = Calendar.getInstance();
        dia = fecha.get(Calendar.DAY_OF_MONTH);
        mes = fecha.get(Calendar.MONTH)+1;
        anio = fecha.get(Calendar.YEAR);
        tvfecha.setText(dia+"/"+mes+"/"+anio);
    }






    private void getImages(){
        mImagesUrls.add(String.valueOf(R.drawable.jordi));
        mNames.add("jordi elegido");
        mImagesUrls.add(String.valueOf(R.drawable.bale));
        mNames.add("bale elegido");
        mImagesUrls.add(String.valueOf(R.drawable.bob));
        mNames.add("bob elegido");
        mImagesUrls.add(String.valueOf(R.drawable.cartas));
        mNames.add("cartas elegido");
        mImagesUrls.add(String.valueOf(R.drawable.hello));
        mNames.add("hello elegido");
        mImagesUrls.add(String.valueOf(R.drawable.patriota));
        mNames.add("patriota elegido");
        mImagesUrls.add(String.valueOf(R.drawable.pistola));
        mNames.add("pistola elegido");
        mImagesUrls.add(String.valueOf(R.drawable.verde));
        mNames.add("verde elegido");

        getRecycleView();


    }

   /* private void getRecycleView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        rvLista = findViewById(R.id.lista);

        rvLista.setLayoutManager(linearLayoutManager);

        adaptadorRecycleView adaptadorRecycleView = new adaptadorRecycleView(mImagesUrls,this);

        rvLista.setAdapter(adaptadorRecycleView);
    }*/


// ...

    private void getRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvLista = findViewById(R.id.lista);
        rvLista.setLayoutManager(linearLayoutManager);

        adaptadorRecycleView adaptadorRecycleView = new adaptadorRecycleView(mImagesUrls, this,mNames);

        // Agrega un listener para manejar los clics en las imágenes
        adaptadorRecycleView.setOnItemClickListener(new adaptadorRecycleView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("actividad_pedido", "Clic en la posición: " + position);
                // Aquí actualizas la variable imagenSeleccionada
                imagenSeleccionada = position;
                // Ahora el método incrementarNumeroEnTextView no se llamará automáticamente al hacer clic en el RecyclerView
                // Puedes dejar este espacio en blanco o realizar otras acciones si es necesario
            }
        });

        rvLista.setAdapter(adaptadorRecycleView);

        // Agrega un SnapHelper para centrar las imágenes
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvLista);
    }




    private void beginXMLparsingComerciales() {
        InputStream is = null;
        try {
            Log.d("XMLFile", "Ubicación del archivo: " + getAssets().open("comerciales.xml"));

            is = getAssets().open("comerciales.xml");
        }catch (IOException e){
            e.printStackTrace();
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;

        try{
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;

        try{
            doc = documentBuilder.parse(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        Element element = doc.getDocumentElement();
        element.normalize();

        NodeList nodeList = doc.getElementsByTagName("comercial");

        for(int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element2 = (Element) node;
                String nombre = element2.getElementsByTagName("nombre").item(0).getTextContent();
                String apell1 = element2.getElementsByTagName("apellido1").item(0).getTextContent();
                String apell2 = element2.getElementsByTagName("apellido2").item(0).getTextContent();
                String dni = element2.getElementsByTagName("dni").item(0).getTextContent();
                String direccion = element2.getElementsByTagName("direccion").item(0).getTextContent();
                String email = element2.getElementsByTagName("email").item(0).getTextContent();
                String zona1 = element2.getElementsByTagName("zona1").item(0).getTextContent();
                String zona2 = element2.getElementsByTagName("zona2").item(0).getTextContent();
                addingValuesToHashMapComerciales(nombre,apell1,apell2,dni,direccion,email,zona1,zona2);



            }
        }

        //ListView lv = findViewById(R.id.idLvJson);
        //ListAdapter adapter = new SimpleAdapter(MainActivity.this,comerciales,R.layout.list_item,new String[]{"nombre","apellido1","apellido2","dni","direccion","email","zona1","zona2"},
        //      new int[]{R.id.idName,R.id.idSurname1,R.id.idSurname2,R.id.idDni,R.id.idDireccion,R.id.idEmail,R.id.idZona1,R.id.idZona2}) ;
        //lv.setAdapter(adapter);


        updateSpinnerComerciales();
    }


    private void updateSpinnerComerciales() {
        // Obtén una referencia al Spinner en tu layout
        Spinner spinnerComerciales = findViewById(R.id.spinnerComerciales);

        // Obtén los nombres de los comerciales para mostrar en el Spinner
        ArrayList<String> nombresComerciales = new ArrayList<>();
        for (HashMap<String, String> comercial : comerciales) {
            nombresComerciales.add(comercial.get("nombre"));
        }

        // Crea un ArrayAdapter para el Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresComerciales);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asigna el ArrayAdapter al Spinner
        spinnerComerciales.setAdapter(spinnerAdapter);

        // Agrega un listener para manejar la selección del Spinner
        spinnerComerciales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Aquí puedes realizar acciones cuando se selecciona un elemento en el Spinner
                // Puedes obtener el comercial seleccionado usando la posición
                // Ejemplo: HashMap<String, String> comercialSeleccionado = comerciales.get(position);
                comercialEleguido = position;
                nombreComercialElegido = comerciales.get(position).get("nombre");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método requerido, pero no necesitas realizar acciones aquí si no lo necesitas
            }
        });
    }


    private void addingValuesToHashMapComerciales(String nombre, String apell1, String apell2, String dni, String direccion, String email, String zona1, String zona2) {
        HashMap<String,String>comercial = new HashMap<>();
        comercial.put("nombre",nombre);
        comercial.put("apellido1",apell1);
        comercial.put("apellido2",apell2);
        comercial.put("dni",dni);
        comercial.put("direccion",direccion);
        comercial.put("email",email);
        comercial.put("zona1",zona1);
        comercial.put("zona2",zona2);

        comerciales.add(comercial);

    }



    private void beginXMLparsingPartners() {
        InputStream is = null;
        try {
            Log.d("XMLFile", "Ubicación del archivo: " + getAssets().open("partners.xml"));

            is = getAssets().open("partners.xml");
        }catch (IOException e){
            e.printStackTrace();
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;

        try{
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;

        try{
            doc = documentBuilder.parse(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        Element element = doc.getDocumentElement();
        element.normalize();

        NodeList nodeList = doc.getElementsByTagName("partner");

        for(int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element2 = (Element) node;
                String id_partners = element2.getElementsByTagName("id_partners").item(0).getTextContent();
                String cif = element2.getElementsByTagName("cif").item(0).getTextContent();
                String direccion = element2.getElementsByTagName("direccion").item(0).getTextContent();
                String telefono = element2.getElementsByTagName("telefono").item(0).getTextContent();
                String email = element2.getElementsByTagName("email").item(0).getTextContent();
                String persona_de_contacto = element2.getElementsByTagName("persona_de_contacto").item(0).getTextContent();
                String id_zona = element2.getElementsByTagName("id_zona").item(0).getTextContent();
                addingValuesToHashMapPartners(id_partners,cif,direccion,telefono,email,persona_de_contacto,id_zona);



            }
        }

        //ListView lv = findViewById(R.id.idLvJson);
        //ListAdapter adapter = new SimpleAdapter(MainActivity.this,comerciales,R.layout.list_item,new String[]{"nombre","apellido1","apellido2","dni","direccion","email","zona1","zona2"},
        //      new int[]{R.id.idName,R.id.idSurname1,R.id.idSurname2,R.id.idDni,R.id.idDireccion,R.id.idEmail,R.id.idZona1,R.id.idZona2}) ;
        //lv.setAdapter(adapter);


        updateSpinnerPartners();
    }


    private void updateSpinnerPartners() {
        // Obtén una referencia al Spinner en tu layout
        Spinner spinnerComerciales = findViewById(R.id.spinnerPartners);

        // Obtén los nombres de los comerciales para mostrar en el Spinner
        ArrayList<String> nombresComerciales = new ArrayList<>();
        for (HashMap<String, String> comercial : partners) {
            nombresComerciales.add(comercial.get("id_partners"));
        }

        // Crea un ArrayAdapter para el Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresComerciales);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Asigna el ArrayAdapter al Spinner
        spinnerComerciales.setAdapter(spinnerAdapter);

        // Agrega un listener para manejar la selección del Spinner
        spinnerComerciales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Aquí puedes realizar acciones cuando se selecciona un elemento en el Spinner
                // Puedes obtener el comercial seleccionado usando la posición
                // Ejemplo: HashMap<String, String> comercialSeleccionado = comerciales.get(position);

                partnerEleguido = position;
                nombrePartnerelegido = partners.get(position).get("id_partners");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método requerido, pero no necesitas realizar acciones aquí si no lo necesitas
            }
        });
    }


    private void addingValuesToHashMapPartners(String id_partners, String cif, String direccion, String telefono, String email, String persona_de_contacto, String id_zona) {
        HashMap<String,String>partner = new HashMap<>();
        partner.put("id_partners",id_partners);
        partner.put("cif",cif);
        partner.put("direccion",direccion);
        partner.put("telefono",telefono);
        partner.put("email",email);
        partner.put("persona_de_contacto",persona_de_contacto);
        partner.put("id_zona",id_zona);


        partners.add(partner);

    }
}
