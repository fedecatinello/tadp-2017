
module CaseClassModule

  def case_class(str, &block)

    # str es una concatenacion (string) entre el simbolo inicial y del que hereda con un ~ en el medio
    # si no hereda str solo tendra el nombre de la clase CaseClass

    arr_sym = str.to_s.split('~') # [ClaseHijo, ClasePadre] array de strings con el nombre de las clases

    case_class = Object.const_get(arr_sym[0].to_s) # CaseClass que se está creando

    case_class.class_eval &block # Se evalua el bloque que llega luego del nombre de la clase para definir metodos y atributos

    instance_variables = case_class.variables # Un array con el nombre de todas las variables que tiene la clase

    if arr_sym.length == 1 # No está heredando

      # define_singleton_method define EN LA CLASE QUE IMPORTE ESTE MODULO (Ejemplo Object) una funcion con
      # el nombre de Case Class que estamos creando, la cual se encarga de tomar los argumentos (valores de los atributos),
      # y delegarle la responsabilidad al initialize de:
      # crear una instancia de la clase con esos argumentos, freezar la instancia y devolverla.
      # Ejemplo:
      #	case_class Persona do attr_accessor :nombre, :edad end (defino una CaseClass Persona)
      # una_instancia = UnaCaseClass('Juan', 32) (hago una instancia de la clase con esos atributos SIN USAR NEW)

      define_singleton_method(case_class.to_s, lambda { |*args| # Metodo con nombre de la clase que crea ..

        # Tirar error si recibe mas parametros que atributos tiene para setear
        if args.length > instance_variables.length
          raise "#{case_class} espera máximo #{instance_variables.length} parámetros y recibió #{args.length}"
        end

        case_class.new(*args)

      })

    elsif arr_sym.length == 2
      puts 'Aca va la logica de heredar'

    else
      puts 'Aca hay que tirar un error extraño'
    end
  end

end

class Object

  include CaseClassModule

  def self.const_missing(name)
    Object.const_set(name.to_s, CaseClass.dup)
  end

=begin
  def self.method_missing(symbol, *args, &block)   TODO: sacar esto si no hace falta
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

  def is_case_class? #TODO: sacar esto si no hace falta
    false
  end

end
