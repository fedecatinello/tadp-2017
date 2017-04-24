require_relative '../lib/modules/CaseClassMixin'

class CaseClassBuilder

  @class_name = nil
  @superclass_name = nil

  def <(superklass)
    @superclass_name = superklass
  end

  def initialize(name)
    @class_name = name
  end

  @class_logic = Proc.new do |klass|

    @variables = nil

    def klass.variables
      @variables
    end

    def klass.attr_accessor (*attrs)
      @variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
      attrs.map do |attr|
        self.send('attr_reader', attr)
        self.instance_variable_set('@' + attr.to_s, ':D')
      end
    end

  end

  def build (&block)

    if @superclass_name
      klass = Class.new(@superclass_name)
    else
      klass = Class.new
    end

    klass.include CaseClassMixin # Incluimos el mixin con la logica de las case_class
    klass.class_eval(&block)

    @class_logic.call(klass) # TODO: falta incluirle logica propia de clase
    klass.new.freeze
  end

end