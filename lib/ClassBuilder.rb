require_relative '../lib/modules/CaseClassMixin'

class CaseClassBuilder

  attr_accessor :klass_name, :superclass_name

  def <(superklass_param)
    self.superclass_name = superklass_param
    self # Retorno self para que la funcion case_class siga recibiendo el CaseClassBuilder
  end

  def build(mixin, class_mixin, &block)

    klass = Class.new(superclass_name || Object)

    klass.include mixin # Incluimos el mixin con la logica a nivel de instancia
    klass.extend class_mixin # Incluimos el mixin con la logica a nivel de clase
    klass.class_eval &block if block_given? # Incluimos la logica del cuerpo de la case_class/case_object

    klass

  end

end