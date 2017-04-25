require_relative '../../lib/ClassBuilder'

module CaseClassModule

  class ::Object

    def self.attr_accessor (*args)
      self.instance_variable_set('@all_attributes', args)
      super
    end

    def self.all_attr
      self.instance_variable_get('@all_attributes')
    end

    def self.const_missing(name)
      builder = CaseClassBuilder.new
      builder.set_class_name(name)
      #Object.const_set(name, builder)
      return builder
    end

    def case_class(builder, &block)

      if !block
        throw 'No recibio bloque'
        return
      end

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

class Padre
  attr_accessor :p1, :p2, :p3
end
class Hijo < Padre
  attr_accessor :h1, :h2
end