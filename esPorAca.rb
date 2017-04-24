class UnPadre
    attr_accessor :var_padre
    def metodo_padre
        'PADRE'
    end
end

module UnMixin
    def hash
        100
    end
    def to_s
        'to_s del caseclass'
    end
end

class CaseClassBuilder

    @@bloque_de_logica = Proc.new do # Asi se guarda un bloque en una variable
        @variables
        def self.attr_accessor(*args)
            puts 'Si esto funciona no deberia crear getters ni setters y variables deberia volver 5'
            @variables = 5
        end
        def variables
            @variables
        end
    end
    
    def self.build
        klass = Class.new
        klass.include(UnMixin)
        klass.class_eval &@@bloque_de_logica
        klass.class_eval do # ESTE ES EL BLOCK QUE VIENE COMO PARAMETRO &block
            attr_accessor :aaa
            def metodo_del_hijo
                'HIJO'
            end
        end
        return klass
    end

    def self.buildConHerencia
        klass = Class.new(UnPadre)
        klass.include(UnMixin)
        klass.class_eval &@@bloque_de_logica
        klass.class_eval do # ESTE ES EL BLOCK QUE VIENE COMO PARAMETRO &block
            attr_accessor :aaa
            def metodo_del_hijo
                'HIJO'
            end
        end
        return klass
    end

end