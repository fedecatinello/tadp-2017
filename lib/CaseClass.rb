class CaseClass

  @variables = nil

  def metodo_de_la_caseclass #TODO: sacar esto si no hace falta
    'hola'
  end

  def self.< (sym)
    return (self.ancestors.first.to_s + '~' + sym.to_s).to_s
  end

  def self.is_case_class? #TODO: sacar esto si no hace falta
    true
  end

  def self.attr_accessor (*attrs)
    @variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
    attrs.map do |attr|
      self.send('attr_reader', attr)
      self.instance_variable_set('@' + attr.to_s, ':D')
    end
  end

  def self.variables
    @variables
  end

  def to_s
    "#{self.class}(#{self.class.variables.map { |var| self.instance_variable_get('@' + var.to_s) }
                         .join(', ')})"

    # aux_string = ''
    # aux_string += self.class.to_s     TODO: sacar si no hace falta
    # aux_string += '('
    # self.class.variables.each{ |var|
    #   aux_string += self.instance_variable_get('@' + var.to_s) + ', '
    # }
    # aux_string = aux_string.chomp(', ')
    # aux_string += ')'
    # aux_string
  end

  def hash
    7 + self.class.variables.inject(0) { |result, var| result + self.instance_variable_get('@' + var.to_s).hash }

    # hash_aux = 7
    # self.class.variables.each{ |var|        TODO: sacar si no hace falta
    #   hash_aux += self.instance_variable_get('@' + var.to_s).hash
    # }
    # hash_aux
  end

  def ==(other_instance) # TODO: Refactorizar
    instance = self

    if (instance.class != other_instance.class)
      return false
    elsif (instance.instance_variables != other_instance.instance_variables)
      return false
    else
      instance.instance_variables.each do |var|
        if(instance.send(var.to_s[1..-1]) != other_instance.send(var.to_s[1..-1])) # string[1..-1] remueve el primer caracter, en este caso un @
          return false
        end
      end
      return true
    end
  end

  def initialize(*args) # Cada vez que hacemos un new sobre una case_class, creará una instancia inmutable

    instance_variables = self.class.variables

    # Por cada argumento que se le pasa a la funcion (valor a setear)
    # Se setea cada variable del array instance_variables con cada argumento del array args en orden

    args.each_with_index do |arg, index|
      self.instance_variable_set("@#{instance_variables[index]}", arg)
    end

    self.freeze

  end

  # Metodos utilizados para evitar sobreescribir hash, == y to_s si es que ya fueron definidos
  # en el cuerpo de la case_class o en sus superclases

  def self.method_missing(method_name, *args, &block)
    (%w[to_s hash ==].include?method_name)?self.send(method_name, *args, &block):super
  end

  def self.respond_to_missing?(method_name, include_private = false)
    (%w[to_s hash ==].include?method_name) || super
  end

  # Metodo utilizado para crear una instancia inmutable a partir de un simbolo con el nombre de la clase
  # y argumentos que representan los atributos de la nueva instancia

  def create_inmutable_instance(case_class, args) # TODO: este metodo no se llama en ningun lado!!!!!

    aux_instance = case_class.new # Instancia que se va a devolver luego de prepararla

    instance_variables = case_class.variables # Un array con el nombre de todas las variables que tiene la clase

    cont = 0
    args.each do |arg| # Por cada argumento que se le pasa a la funcion (valor a setear)
      # Se setea cada variable del array instance_variables con cada argumento del array args en orden
      aux_instance.instance_variable_set('@' + instance_variables[cont].to_s, arg)
      cont += 1
    end

    # Finalmente se freeza la instancia y se devuelve
    aux_instance.freeze
  end

  # Metodo utilizado para el copiado inteligente de instancias

  def copy(*args) # args pueden ser una o mas funciones lambda

    new_instance = self.dup

    args.each do |lam| # Checkeo que todas las lambda sean de aridad 1
      if lam.arity != 1
        raise 'Las funciones lambda deben ser de aridad 1.'
      end
    end

    args.each do |lam|
      # El unico argumento de cada lambda es el nombre del atributo que quiere modificar

      attr_name = lam.parameters.first.last.to_s # parameters es un array asi: [[:req, :nombre_de_variable]] por eso .first.last

      # La lambda solo se aplica si el parametro (nombre de variable a modificar) esta definida en la instancia,
      # si no se podrian crear nuevas variables a una instancia pasando en la lambda un nombre que no existe como
      # variable (porque se hace un instance_variable_set).
      if self.instance_variable_defined? ('@'+attr_name)
        new_instance.instance_variable_set('@'+attr_name, lam[self.instance_variable_get('@'+attr_name)]) # lam[] aplica la funcion a un valor
      end

    end

    new_instance.freeze

  end

end

