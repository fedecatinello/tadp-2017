require 'rspec'

=begin
describe '' do

  before do
  end

  describe '' do
    it '' do
      expect().to eql()
    end
  end
end
=end

describe 'Modelado basico de una case class' do

  before do
    require_relative('../lib/modules/SyntaxModule.rb')
  end

  describe 'Requerimientos minimos' do

    it 'tipo 1' do
      module M
        def m1() 5 end
      end

      class C
        def m2() 7 end
      end

      case_class X < C do
        include M
        def self.m3() 9 end
      end

      un_x = X.new
      expect(un_x.m1).to eql(5) # Retorna 5
      expect(un_x.m2).to eql(7) # Retorna 7
      expect(X.m3).to eql(9) # Retorna 9
    end

    it 'Hereda de C' do
      expect(X.superclass).to eql(C)
    end

    it 'Es de la clase X' do
      un_x = X.new
      expect(un_x.class).to eql(X)
    end

  end
end

describe 'Funcion Constructora' do

  before do

    case_class Alumno do
      attr_accessor :nombre, :nota
    end

    case_class Curso do
      attr_accessor :nombre, :codigo, :alumnos
    end

  end

  describe 'Funcion Contructora' do
    it 'instanciacion de una case class' do

      curso = Curso("TADP", "k3031", [
          Alumno("Jose", 8),
          Alumno.new("Miguel", 2)
      ])

      expect(curso.nombre).to eql('TADP')
      expect(curso.codigo).to eql('k3031')

      #MANDARLE MENSAJES DE METODOS QUE NO ENTIENDE PARA QUE DE ERROR

      expect(curso[0].nombre).to eql('Jose')
      expect(curso[0].nota).to eql(8)

      expect(curso[1].nombre).to eql('Miguel')
      expect(curso[1].nota).to eql(2)

      #COMPARAR CON VALORES ERRADOS PARA QUE DE ERROR
    end
  end
end
#
# describe 'Instancias Inmutables' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :nota
#
#       def hacer_trampa
#         @nota = 10
#       end
#     end
#   end
#
#   describe 'Instancias Inmutable' do
#     it 'creacion instrancia inmutable' do
#
#       alumno = Alumno("Jose", 8)
#       expect(alumno.nombre).to eql('Jose')
#
#       #MOSTRAR QUE LOS SIGUIENTES CASOS DAN ERROR
#       #alumno.nombre = 10  <-- Error!
#       #alumno.hacer_trampa <-- Error!
#
#     end
#   end
# end
#
# describe 'Buenos Defaults' do
#
#   before do
#
#     case_class Persona do
#       attr_accessor :nombre, :apellido, :edad
#     end
#
#     case_class Curso do
#       attr_accessor :nombre, :codigo, :alumnos
#     end
#
#     case_class Alumno do
#       attr_accessor :nombre, :nota
#     end
#   end
#
#   describe 'Buenos Defaults' do
#     it 'to_s' do
#       persona_a = Persona.new('Juan', 'Carlos', 37)
#       persona_b = Persona.new('Juan', 'Carlos')
#       expect(persona_a.to_s).to eql('Persona(Juan, Carlos, 37)')
#       expect(persona_b.to_s).to eql('Persona(Juan, Carlos, nil)')
#       # en la definicion de to_s value recibe [persona_a, persona_b] y hace el to_s de ese array
#       # habria que iterar el array y a esos valores hacer .to_s (persona_a, persona_b y no su array)
#       curso_tadp = Curso.new('TADP', 514, [persona_a, persona_b])
#       expect(curso_tadp.to_s)
#           .to eql('Curso(TADP, 514, [Persona(Juan, Carlos, 37), Persona(Juan, Carlos, nil)])')
#     end
#
#     it '==' do
#
#       alumno = Alumno("Jose", 8)
#       otro_alumno_igual = Alumno("Jose", 8)
#       otro_alumno_distinto = Alumno("Miguel", 2)
#
#       expect(alumno == otro_alumno_igual).to eql(true)
#       expect(alumno == otro_alumno_distinto).to eql(false)
#
#     end
#
#     it 'hash' do
#       alumno = Alumno("Jose", 8)
#       hash_comun = 'Jose'.hash + 8.hash
#       expect(alumno.hash).to eql(-799690864674641430)
#       expect(alumno.hash == hash_comun).to eql(false)
#     end
#   end
#
#   before do
#
#     module M
#       def to_s()
#         "Soy un M"
#       end
#     end
#
#     class C
#       def to_s()
#         "Soy un C"
#       end
#     end
#
#     case_class X < C do
#       # no paso nada
#     end
#
#     case_class Y do
#         include M
#     end
#
#     case_class Z do
#       def to_s()
#         "Soy un Z"
#       end
#     end
#   end
#   describe 'Herencia en Buenos Defaults' do
#
#     expect(X().to_s).to eql('Soy un C')
#     expect(Y().to_s).to eql('Soy un M')
#     expect(Z().to_s).to eql('Soy un Z')
#
#   end
# end
#
# describe 'Copiado Inteligente' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :nota
#     end
#
#   end
#
#   describe 'Copiado Inteligente' do
#     it 'tests' do
#
#       alumno = Alumno("Jose", 8)
#       otro_alumno = alumno.copy
#       expect(alumno == otro_alumno).to eql(true)
#
#       otro_alumno = alumno.copy ->(nota){nota + 1}
#       expect(otro_alumno.nombre).to eql('Jose')
#       expect(otro_alumno.nota).to eql(9)
#
#       otro_alumno_mas = alumno.copy ->(nombre){"Arturo"}, ->(nota){5}
#       expect(otro_alumno_mas.nombre).to eql('Arturo')
#       expect(otro_alumno_mas.nota).to eql(5)
#
#       # MOSTRAR QUE LA SIGUIENTE LINEA DEBERIA DA ERROR
#       # alumno.copy ->(edad){25} # Error! No existe el atributo "edad"
#
#     end
#   end
# end
#
# describe 'Case Objects' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :estado
#     end
#
#     case_object Cursando do
#     end
#
#     case_class Termino do
#       attr_accessor :nota
#     end
#
#   end
#
#   describe 'Case Objects' do
#     it 'tests' do
#       # TODO: HACER ESTOS TESTS !
#
#       #expect().to eql()
#     end
#   end
# end
#
# describe 'Pattern Matching' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :estado
#     end
#
#     case_class Termino do
#       attr_accessor :nota
#     end
#
#     case_object Aprobo do
#     end
#
#   end
#
#   describe 'cualquier cosa' do
#     it 'test' do
#       alumno = Alumno("Jose", 8)
#       expect(_.=== alumno).to eql(true)
#       expect(_.=== 10).to eql(true)
#       expect(_.=== 'sarlanga').to eql(true)
#       expect(_.=== Object).to eql(true)
#     end
#   end
#
#   describe 'pertenece a un tipo' do
#     it 'test' do
#       alumno = Alumno("Jose", 9)
#       expect((is_a Array).=== alumno).to eql(false)
#       expect((is_a Alumno).=== alumno).to eql(true)
#     end
#   end
#
#   describe 'tener cierto valor en un atributo' do
#     it 'test' do
#       alumno = Alumno("Jose", 9)
#       expect((has(:nombre, "Raul")).=== alumno).to eql(false)
#       expect((has(:apellido, nil)).=== alumno).to eql(false)
#       expect((has(:nota, 9)).=== alumno).to eql(true)
#     end
#   end
#
#   describe 'comparacion estructural' do
#     it 'test' do
#       alumno = Alumno("Jose", Termino(9))
#       expect(Alumno(“Jose”, Termino(7)).=== alumno).to eql(false)
#       expect((Alumno(“Jose”, Aprobo)).=== alumno).to eql(false)
#       expect(Alumno(_, has(:nota, 9)).=== alumno).to eql(true)
#     end
#   end
# end

