require_relative '../lib/modules/CaseClassMixin'

class CaseClassBuilder

  @@block_logic = Proc.new do # Logica que tienen todas las CaseClass

    def self.attr_accessor (*attrs)

      self.class_variable_set('@@variables', attrs) # Guardo los atributos en una variable de clase para poder leerlos despues
      attrs.map do |attr|
        self.send('attr_reader', attr) # Creo getters, no setters
      end

      # HAY QUE ENCONTRAR SU SUPERCLASE   superklass = ?
      if superklass

      end

    end

    def initialize (*attrs)
      variables = self.class.class_variable_get('@@variables')
      variables.each_with_index { |var, i|
        self.instance_variable_set('@'+var.to_s, attrs[i])
      }
      self.freeze
    end
  end

  def <(superklass)
    self.instance_variable_set('@superclass', superklass)
    self # Retorno self para que la funcion case_class siga recibiendo el CaseClassBuilder
  end

  def get_superclass
    if self.instance_variable_defined? '@superclass'
      return self.instance_variable_get '@superclass'
    else
      return false
    end

  end

  def set_class_name(name)
    self.instance_variable_set('@class', name)
  end

  def get_class_name
    self.instance_variable_get('@class')
  end

  def build (&block)

    superklass = self.get_superclass

    if superklass
      klass = Class.new superklass
    else
      klass = Class.new
    end

    klass.include CaseClassMixin # Incluimos el mixin con la logica de las case_class
    klass.class_eval &@@block_logic # Incluimos la logica de clase
    klass.class_eval &block if block_given?   # Incluimos la logica del cuerpo de la case_class

    klass

  end

end