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
      expect(@un_saludador).to respond_to('saludar')
      expect(@un_saludador.saludar).to eql('hola')
    end

    it 'Deberia crear getters y no setters' do
      expect(@una_case_class).to respond_to('a')
      expect(@una_case_class).to_not respond_to('a=')
    end

    it 'Hereda de C' do
      expect(UnaCaseClass).to be < UnaClasePadre
    end

    it 'Es de la clase X' do
      expect(@una_case_class.class).to be(UnaCaseClass)
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

      @jose = Alumno('Jose', 8)
      @miguel = Alumno.new('Miguel', 2)

      @curso = Curso('TADP', 'k3031', [@jose, @miguel])
    end

    it 'Instanciacion de una case class' do
      expect(@curso.nombre).to eql('TADP')
      expect(@curso.codigo).to eql('k3031')

      expect(@curso.alumnos[0].nombre).to eql('Jose')
      expect(@curso.alumnos[1].nota).to eql(2)
    end

    it 'Instancia inmutables' do
      expect(@jose).to_not respond_to('nombre=')
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
          'Soy un MM'
        end
      end

      class CC
        def to_s()
          'Soy un CC'
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
          'Soy un ZZ'
        end
      end

      @persona_a = Persona.new('Juan', 'Carlos', 37)
      @persona_b = Persona.new('Juan', 'Carlos')

      @alumno = Alumno.new('Jose', 8)
      @otro_alumno_igual = Alumno.new('Jose', 8)
      @otro_alumno_distinto = Alumno.new('Miguel', 2)

      @curso_tadp = Curso.new('TADP', 514, [@persona_a, @persona_b])
    end

    it 'to_s con todos los atributos seteados' do
      expect(@persona_a.to_s).to eql('Persona(Juan, Carlos, 37)')
    end
    it 'to_s con menos atributos seteados' do
      expect(@persona_b.to_s).to eql('Persona(Juan, Carlos, nil)')
    end

    it 'to_s con atributos case_class' do
      expect(@curso_tadp.to_s)
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

    it 'Herencia en Buenos Defaults' do
      expect(XX.new().to_s).to eql('Soy un CC')
      expect(YY.new().to_s).to eql('Soy un MM')
      expect(ZZ.new().to_s).to eql('Soy un ZZ')
    end
  end
  describe 'Copiado Inteligente' do

    before :all do
      @alumno = Alumno.new('Jose', 8)
      @otro_alumno = @alumno.copy
    end

    it 'Sus valores son iguales' do
      expect(@otro_alumno.nombre).to eql('Jose')
      expect(@otro_alumno.nota).to eql(8)
    end
    it 'Modifico atributos con una lambda de aridad 1' do
      expect((@alumno.copy -> (nota){nota + 1}).nota).to eql(9)
      expect((@alumno.copy -> (nombre){'Juan'}).nombre).to eql('Juan')
    end
    it 'Con lambdas de aridad distinta de uno deberia fallar' do
      expect{@alumno.copy -> (nota, edad){ 'do something' }}.to raise_error
    end

  end

end

describe 'Case Objects' do
  before :all do

    case_object UnCaseObject do
      def metodo_conocido()
        'Metodo de un case object'
      end
    end

  end

  it 'Funcionalidad basica de case object' do
    expect(UnCaseObject).to respond_to('metodo_conocido')
    expect(UnCaseObject.metodo_conocido).to start_with('Metodo')
    expect(UnCaseObject).to be_a_kind_of(CaseClassMixin) # Se comportan como case_class
  end

  it 'attr_accessor deberia tirar error' do   # Los case_object no admiten atributos
    expect{case_object UnCaseObj do attr_accessor :a, :b end}.to raise_error(UncaughtThrowError)
  end

  it 'El copy de un CaseObject es su propia instancia' do
    @otro_case_object = UnCaseObject.copy
    expect(@otro_case_object).to be(UnCaseObject)
  end

  it 'to_s de un case object sin parentesis' do
    expect(UnCaseObject.to_s).to eql('UnCaseObject')
  end

end

describe 'Pattern Matching' do

  before :all do

    case_class Termino do
      attr_accessor :nota
    end

    case_object Aprobo do
    end

    @alumno = Alumno.new('Jose', 9)
    @otro_alumno = Alumno.new('Jose', Termino.new(9))

  end

  it 'Any pattern' do
    x = case @alumno
          when _
            5
        end
    expect(x).to eql(5)
  end

  it 'Belong Pattern' do
    valor = case @alumno
              when (is_a Array)  # El patrón falla: alumno no es un array
                5
              when (is_a Alumno) # El patrón pasa: alumno es de tipo alumno
                7
            end
    expect(valor).to eql(7)
  end

  it 'Contains Pattern' do
    valor = case @alumno
              when has(:nombre, "Raul") # El patrón falla: el nombre no es "Raul"
                5
              when has(:apellido, nil)  # El patrón falla: no hay atributo apellido
                7
              when has(:nota, 9)        # El patrón matchea
                3
            end
    expect(valor).to eql(3)
  end

  it 'Estructural Pattern' do
    valor = case @otro_alumno
              when Alumno.new('Jose', Termino.new(7)) # Falla: la nota no coincide.
                5
              when Alumno.new('Jose', Aprobo)     # Falla: el estado no coincide.
                7
              when Alumno.new(_, has(:nota, 9))   # Pasa! el nombre no importa y el estado tiene nota 9.
                3
            end
    expect(valor).to eql(3)
  end

end