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

      # Defino un metodo con el mismo nombre de la clase para poder llamarla directamente y hacer el new
      # Funcion Constructora (1a)
      define_singleton_method(builder.get_class_name, lambda { |*attrs|
        klass = Object.const_get(builder.get_class_name)
        instance = klass.new.dup # dup para que pierda el freeze
        ivars = instance.instance_variables

        if attrs.length > ivars.length
          # TODO: Poner un error mas lindo
          throw 'Cantidad de argumentos incorrecta, se esperaban máximo: ' + ivars.length.to_s
        end

        attrs.each_with_index { |attr, i|
          instance.send('instance_variable_set', ivars[i].to_s, attr)
        }

        instance.freeze
      })
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