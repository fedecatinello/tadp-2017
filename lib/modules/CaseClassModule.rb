require_relative '../../lib/ClassBuilder'

module CaseClassModule

  class ::Object

    def self.const_missing(name)
      builder = CaseClassBuilder.new
      Object.const_set(name, builder)
      builder.set_class_name(name)
      return builder
    end

    def case_class(builder, &block)
      Object.const_set(builder.get_class_name, builder.build(&block))
    end

  end

end

class Persona
  attr_accessor :nombre, :edad
  def saludar
    'Hola'
  end
end
case_class Alumno < Persona do
  attr_accessor :carrera
  def metodo_alumno
    'Alumno'
  end
end

case_class CC_X do
  attr_accessor :asd, :dsa
  def metodo_x
    'X'
  end
  def trampa # Si esta freezada no me deberia dejar hacer esto.
    @asd = 100
  end
end