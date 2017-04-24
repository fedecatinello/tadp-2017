require_relative '../lib/modules/CaseClassMixin'

class CaseClassBuilder

  @class_name = nil
  @superclass_name = nil

  @@block_logic = Proc.new do

    @variables = nil

    def variables
      @variables
    end

    def self.attr_accessor (*attrs)
      @variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
      attrs.map do |attr|
        self.send('attr_reader', attr)
        self.instance_variable_set('@' + attr.to_s, ':D')
      end
    end

  end

  def <(superklass)
    @superclass_name = superklass
  end

  def initialize(name)
    @class_name = name
  end

  def build (&block)

    if @superclass_name
      klass = Class.new @superclass_name
    else
      klass = Class.new
    end

    klass.include CaseClassMixin # Incluimos el mixin con la logica de las case_class
    klass.class_eval &@@block_logic # Incluimos la logica de clase
    klass.class_eval &block if block_given?   # Incluimos la logica del cuerpo de la case_class
    klass
  end

end