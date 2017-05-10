require_relative 'CaseClassMixin'
require_relative 'CaseObjectMixin'
require_relative 'PatternModule'
require_relative '../ClassBuilder'

module SyntaxModule

  class ::Object

    include PatternModule

    def self.const_missing(name)
      builder = CaseClassBuilder.new
      builder.klass_name = name
      builder
    end

    def case_object(builder, &block)
      Object.const_set(builder.klass_name, builder.build(CaseObjectMixin, CaseObjectClassMixin, &block).new)
    end

    def case_class(builder, &block)

      unless block_given?
        throw 'ArgumentMismatchError: se esperaba un bloque'
      end

      x = builder
      y = builder.klass_name

      Object.const_set(builder.klass_name, builder.build(CaseClassMixin, CaseClassClassMixin, &block))

      # Funcion Constructora (1a)
      add_constructor_syntax(builder)

    end

    private

    def add_constructor_syntax(builder)

      define_singleton_method(builder.klass_name, lambda { |*attrs|
        klass = Object.const_get(builder.klass_name)
        ivars = klass.variables
        instance = klass.new.dup # dup para que pierda el freeze

        if attrs.length > ivars.length
          throw "ArgumentMismatchError: se esperaban #{ivars.length} argumentos como máximo"
        end

        attrs.each_with_index do |attr, i|
          instance.send('instance_variable_set', "@#{ivars[i].to_s}", attr)
        end

        instance.freeze
      })

    end

  end

end