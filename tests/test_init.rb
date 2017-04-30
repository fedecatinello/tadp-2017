require_relative '../lib/modules/SintaxModule.rb'

class Padre
  attr_accessor :p1, :p2, :p3
  def metodo_padre
    "SOY EL PADRE"
  end

  def to_s
    'to_s viene del padre'
  end
end

module M
  def metodo_m
    "SOY DEL MIXIN M"
  end
end

case_class CC_A < Padre do
  attr_accessor :h1, :h2
  include M
  def metodo_hijo
    "SOY EL HIJO A"
  end
end

case_class CC_B do
  attr_accessor :nombre, :apellido

  include M
  def metodo_hijo
    "SOY EL HIJO B"
  end

  def ==(ele)
    false
  end

end

case_class Alumno do
  attr_accessor :nombre, :nota
end
case_class Curso do
  attr_accessor :materia, :codigo, :alumnos
end

case_object Cursando do
  def nota
    9
  end
end

# module M
#   def to_s() 
#     "Soy un M"
#   end
# end
# class C
#   def to_s() 
#     "Soy un C"
#   end
#   def saludarC
#     'HOLA!!'
#   end
# end
# case_class X < C do
#   attr_accessor :a
#   def to_s
#     'TO S DE X'
#   end
#   def hash
#     'HASH DE X'
#   end
#   def == (a)
#     '== DE X'
#   end
# end
# case_class Y do
#   include M
# end
# case_class Z do
#   def to_s()
#     "Soy un Z"
#   end
# end
# case_class N do
#   attr_accessor :a, :b, :c, :d
# end