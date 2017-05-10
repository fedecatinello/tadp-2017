require 'rspec'
require 'spec_helper.rb'

describe 'Case Classes' do

  before :all do

    module UnMixin
      def m1() 5 end
    end

    class UnaClasePadre
      def m2() 7 end
    end

    case_class UnaCaseClass < UnaClasePadre do
      include UnMixin
      attr_accessor :a, :b
      def self.m3() 9 end
    end

    case_class Saludador do
      def saludar
        'hola'
      end
    end

    @una_case_class = UnaCaseClass.new
    @un_saludador = Saludador.new

  end

  describe 'Funcionalidad basica' do

    it 'Validando metodos' do
      expect(@una_case_class.m1).to eql(5) # Retorna 5
      expect(@una_case_class.m2).to eql(7) # Retorna 7
      expect(UnaCaseClass.m3).to eql(9) # Retorna 9
      @un_saludador.should respond_to('saludar')
      expect(@un_saludador.saludar).to eql('hola')
    end

    it 'Deberia crear getters y no setters' do
      @una_case_class.should respond_to('a')
      @una_case_class.should_not respond_to('a=')
    end

    it 'Hereda de C' do
      expect(UnaCaseClass.superclass).to eql(UnaClasePadre)
    end

    it 'Es de la clase X' do
      expect(@una_case_class.class).to eql(UnaCaseClass)
    end

  end

  describe 'Funcion Constructora' do

    before :all do

      case_class Alumno do
        attr_accessor :nombre, :nota
        def hacer_trampa
          @nota = 10
        end
      end

      case_class Curso do
        attr_accessor :nombre, :codigo, :alumnos
      end

      @jose = Alumno("Jose", 8)
      @miguel = Alumno.new("Miguel", 2)

      @curso = Curso("TADP", "k3031", [@jose, @miguel])
    end

    it 'Instanciacion de una case class' do
      expect(@curso.nombre).to eql('TADP')
      expect(@curso.codigo).to eql('k3031')

      expect(@curso.alumnos[0].nombre).to eql('Jose')
      expect(@curso.alumnos[1].nota).to eql(2)
    end

    it 'Instancia inmutables' do
      @jose.should_not respond_to('nombre=')
      expect{@miguel.nombre = 10}.to raise_error(NoMethodError)
      expect{@miguel.hacer_trampa}.to raise_error(RuntimeError)
    end

  end

  describe 'Buenos Defaults' do

    before :all do
      case_class Persona do
        attr_accessor :nombre, :apellido, :edad
      end

      module MM
        def to_s()
          "Soy un MM"
        end
      end

      class CC
        def to_s()
          "Soy un CC"
        end
      end

      case_class XX < CC do
        # no paso nada
      end

      case_class YY do
        include MM
      end

      case_class ZZ do
        def to_s()
          "Soy un ZZ"
        end
      end

      @persona_a = Persona.new('Juan', 'Carlos', 37)
      @persona_b = Persona.new('Juan', 'Carlos')

      @alumno = Alumno.new("Jose", 8)
      @otro_alumno_igual = Alumno.new("Jose", 8)
      @otro_alumno_distinto = Alumno.new("Miguel", 2)

    end

    it 'to_s con todos los atributos seteados' do
      expect(@persona_a.to_s).to eql('Persona(Juan, Carlos, 37)')
    end
    it 'to_s con menos atributos seteados' do
      expect(@persona_b.to_s).to eql('Persona(Juan, Carlos, nil)')
    end
    it 'to_s con atributos case_class' do
      # en la definicion de to_s value recibe [persona_a, persona_b] y hace el to_s de ese array
      # habria que iterar el array y a esos valores hacer .to_s (persona_a, persona_b y no su array)
      curso_tadp = Curso.new('TADP', 514, [@persona_a, @persona_b])
      expect(curso_tadp.to_s)
          .to eql('Curso(TADP, 514, [Persona(Juan, Carlos, 37), Persona(Juan, Carlos, nil)])')
    end

    it '==' do

      expect(@alumno == @otro_alumno_igual).to eql(true)
      expect(@alumno == @otro_alumno_distinto).to eql(false)

    end

    it 'hash' do
      hash_comun = 'Jose'.hash + 8.hash + 7
      expect(@alumno.hash).to eql(hash_comun)
    end
#
    it 'Herencia en Buenos Defaults' do
      expect(XX.new().to_s).to eql('Soy un CC')
      expect(YY.new().to_s).to eql('Soy un MM')
      expect(ZZ.new().to_s).to eql('Soy un ZZ')
    end
  end
  describe 'Copiado Inteligente' do

    before :all do
      @alumno = Alumno.new("Jose", 8)
      @otro_alumno = @alumno.copy
    end

    it 'Sus valores son iguales' do
      expect(@otro_alumno.nombre).to eql("Jose")
      expect(@otro_alumno.nota).to eql(8)
    end
    it 'Modifico atributos con una lambda de aridad 1' do
      expect((@alumno.copy -> (nota){nota + 1}).nota).to eql(9)
      expect((@alumno.copy -> (nombre){'Juan'}).nombre).to eql('Juan')
    end
    it 'Con lambdas de aridad distinta de uno deberia fallar' do
      expect{@alumno.copy -> (nota, edad){ ':(' }}.to raise_error
    end

  end

end