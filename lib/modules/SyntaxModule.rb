require_relative 'CaseClassMixin'
require_relative 'CaseObjectMixin'
require_relative '../ClassBuilder'

module SyntaxModule

  class ::Object

    def self.const_missing(name)
      builder = CaseClassBuilder.new
      builder.set_class_name(name)
      builder
    end

    def case_object(builder, &block)
      Object.const_set(builder.get_class_name, builder.build(CaseObjectMixin, &block).new)
    end

    def case_class(builder, &block)

      unless block_given?
        throw 'ArgumentMismatchError: se esperaba un bloque'
      end

      Object.const_set(builder.get_class_name, builder.build(CaseClassMixin, &block))

      # Defino un metodo con el mismo nombre de la clase para poder llamarla directamente y hacer el new
      # Funcion Constructora (1a)
      define_singleton_method(builder.get_class_name, lambda { |*attrs|
        klass = Object.const_get(builder.get_class_name)
        instance = klass.new.dup # dup para que pierda el freeze
        ivars = instance.instance_variables

        if attrs.length > ivars.length
          # TODO: Poner un error mas lindo
          throw "ArgumentMismatchError: se esperaban #{ivars.length} argumentos como máximo"
        end

        attrs.each_with_index do |attr, i|
          instance.send('instance_variable_set', ivars[i].to_s, attr)
        end

        instance.freeze
      })
    end

  end

end
