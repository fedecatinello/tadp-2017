
module CaseClassModule

  def case_class(str, &block)

    # str es una concatenacion (string) entre el simbolo inicial y del que hereda con un ~ en el medio
    # si no hereda str solo tendra el nombre de la clase CaseClass

    arr_sym = str.to_s.split('~') # [ClaseHijo, ClasePadre] array de strings con el nombre de las clases

    case_class = Object.const_get(arr_sym[0].to_s) # CaseClass que se está creando

    if arr_sym.length == 1 # No está heredando

      case_class.class_eval &block # Se evalua el bloque que llega luego del nombre de la clase para definir metodos y atributos

      instance_variables = case_class.get_variables # Un array con el nombre de todas las variables que tiene la clase

      # define_singleton_method define EN LA CLASE QUE IMPORTE ESTE MODULO (Ejemplo Object) una funcion con
      # el nombre de Case Class que estamos creando, la cual se encarga de tomar los argumentos (valores de los atributos),
      # crear una instancia de la clase con esos argumentos, freezar la instancia y devolverla. Para luego usarlo de la siguiente manera:
      # Ejemplo:
      #	case_class Persona do attr_accessor :nombre, :edad end (defino una CaseClass Persona)
      # una_instancia = UnaCaseClass('Juan', 32) (hago una instancia de la clase con esos atributos SIN USAR NEW)

      define_singleton_method(case_class.to_s, lambda { |*args| # Metodo con nombre de la clase que hace ..

        # Tirar error si recibe mas parametros que atributos tiene para setear
        if args.length > instance_variables.length
          raise case_class.to_s + ' espera maximo ' + instance_variables.length.to_s + ' parametros y recibió ' + args.length.to_s
        end

        create_inmutable_instance(case_class, args)

      })

    elsif arr_sym.length == 2
      puts 'Aca va la logica de heredar'
    else
      puts 'Aca hay que tirar un error extraño'
    end
  end

  private

  def create_inmutable_instance(case_class, args)

    aux_instance = case_class.new # Instancia que se va a devolver luego de prepararla

    instance_variables = case_class.get_variables # Un array con el nombre de todas las variables que tiene la clase

    cont = 0
    args.map { |arg| # Por cada argumento que se le pasa a la funcion (valor a setear)
      # Se setea cada variable del array instance_variables con cada argumento del array args en orden
      aux_instance.instance_variable_set('@' + instance_variables[cont].to_s, arg)
      cont += 1
    }

    # Finalmente se freeza la instancia y se devuelve
    aux_instance.freeze
  end

end

class Object

  include CaseClassModule

  def self.const_missing(name)
    Object.const_set(name.to_s, CaseClass.dup)
  end

=begin
  def self.method_missing(symbol, *args, &block)
    if symbol.is_case_class?
      puts symbol.to_s + 'is case class'
      #Object.const_get(symbol.to_s).class_eval &block
    else
      puts 'super'
    end
  end

  def self.respond_to_missing?(symbol, include_all=false)
    true
  end
=end

  def is_case_class?
    false
  end

end
