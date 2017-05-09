module CaseClassMixin

  def initialize(*attrs)
    ivars = self.class.variables || []
    ivars.each_with_index { |var, i|
      self.instance_variable_set('@'+var.to_s, attrs[i])
    }
    self.freeze
  end

  def is_method_in_ancestors?(method)

    ancestors = self.class.ancestors

    result = ancestors[(ancestors.index CaseClassMixin)+1..(ancestors.index Object)-1]

    result.any? do |ancestor|
      ancestor.instance_methods(false).include? method
    end

  end

  # Metodos utilizados para los buenos defaults

  def to_s

    if self.is_method_in_ancestors? :to_s
      super
    else
      variables = self.instance_variables
      if variables.length
        result = self.class.to_s + '('
        self.instance_variables.each_with_index { |var, i|
          value = self.instance_variable_get(var)
          result += value == nil ? 'nil' : value.to_s
          if i + 1 < variables.length
            result += ', '
          end
        }
        result += ')'
        result
      else
        self.class.to_s + '()'
      end
    end
  end

  def hash
    self.instance_variables.inject(7) { |result, var| result + self.instance_variable_get(var).hash }
  end

  def ==(other_instance)
    instance = self

    if (instance.class != other_instance.class)
      return false
    end


    if (instance.instance_variables != other_instance.instance_variables)
      return false
    end

    instance.instance_variables.all? do |var|
      instance.send(var.to_s[1..-1]) != other_instance.send(var.to_s[1..-1]) # string[1..-1] remueve el primer caracter, en este caso un @
    end
  end

  # Metodo utilizado para el copiado inteligente de instancias

  def copy(*args) # args pueden ser una o mas funciones lambda

    new_instance = self.dup

    args.each do |lam| # El unico argumento de cada lambda es el nombre del atributo que quiere modificar

      if lam.arity != 1
        throw 'ArgumentMismatchError: Las funciones lambda deben ser de aridad 1.'
      end

      attr_name = lam.parameters.first.last.to_s # parameters es un array asi: [[:req, :nombre_de_variable]] por eso .first.last

      # La lambda solo se aplica si el parametro (nombre de variable a modificar) esta definida en la instancia,
      # si no se podrian crear nuevas variables a una instancia pasando en la lambda un nombre que no existe como
      # variable (porque se hace un instance_variable_set).
      if self.instance_variable_defined?('@'+attr_name)
        new_instance.instance_variable_set('@'+attr_name, lam[self.instance_variable_get('@'+attr_name)]) # lam[] aplica la funcion a un valor
      end

    end

    new_instance.freeze

  end

  # Comparacion estructural
  def ===(obj) # Me llega Alumno("Jose", Termino(9))

    unless obj.is_a?self.class
     return false
    end

    self.instance_variables.all? do |attr_name|
      val_obj = obj.instance_variable_get attr_name
      val_src = self.instance_variable_get attr_name
      val_src === val_obj
    end

  end

end

module CaseClassClassMixin

  def inherited(subklass)
    Object.send :remove_const, subklass.to_s
    throw 'No se puede heredar de una CaseClass.'
  end

  attr_reader :variables
  def attr_accessor(*attrs)
    @variables = attrs # Guardo los atributos en una variable leerlos de afuera
    self.send('attr_reader', *attrs)
  end

end